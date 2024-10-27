package com.onezol.vertex.framework.component.storage.client.s3;


import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.onezol.vertex.framework.common.util.StringUtils;
import com.onezol.vertex.framework.component.storage.client.AbstractFileStorageClient;

import java.io.ByteArrayInputStream;

import static com.onezol.vertex.framework.common.constant.Constants.PROTOCOL_HTTP;
import static com.onezol.vertex.framework.common.constant.Constants.PROTOCOL_HTTPS;
import static com.onezol.vertex.framework.component.storage.client.s3.S3FileStorageClientConfig.ENDPOINT_ALIYUN;
import static com.onezol.vertex.framework.component.storage.client.s3.S3FileStorageClientConfig.ENDPOINT_TENCENT;

/**
 * 基于 S3 协议的文件客户端，实现 MinIO、阿里云OSS、腾讯云COS等云服务
 * <p>
 */
public class S3FileStorageClient extends AbstractFileStorageClient<S3FileStorageClientConfig> {

    private AmazonS3 client;

    public S3FileStorageClient(S3FileStorageClientConfig config) {
        super(config);
    }

    @Override
    protected void doInit() {
        // 补全 domain
        if (StringUtils.isBlank(config.getDomain())) {
            config.setDomain(buildDomain());
        }
        // 初始化客户端
        AWSCredentialsProvider awsCredentialsProvider = new AWSStaticCredentialsProvider(new BasicAWSCredentials(config.getAccessKey(), config.getAccessSecret()));
        client = AmazonS3ClientBuilder.standard()
                .withCredentials(awsCredentialsProvider)
                .withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration(buildEndpointURL(), buildRegion()))
//                .enablePathStyleAccess()
                .build();
    }

    /**
     * 基于 endpoint 构建调用云服务的 URL 地址
     *
     * @return URI 地址
     */
    private String buildEndpointURL() {
        // 如果已经是 http 或者 https，则不进行拼接.主要适配 MinIO
        if (config.getEndpoint().startsWith(PROTOCOL_HTTP) || config.getEndpoint().startsWith(PROTOCOL_HTTPS)) {
            return config.getEndpoint();
        }
        return String.format("https://%s", config.getEndpoint());
    }

    /**
     * 基于 bucket + endpoint 构建访问的 Domain 地址
     *
     * @return Domain 地址
     */
    private String buildDomain() {
        // 如果已经是 http 或者 https，则不进行拼接.主要适配 MinIO
        if (config.getEndpoint().startsWith(PROTOCOL_HTTP) || config.getEndpoint().startsWith(PROTOCOL_HTTPS)) {
            return String.format("%s/%s", config.getEndpoint(), config.getBucket());
        }
        // 阿里云、腾讯云、华为云都适合。七牛云比较特殊，必须有自定义域名
        return String.format("https://%s.%s", config.getBucket(), config.getEndpoint());
    }

    /**
     * 基于 bucket 构建 region 地区
     *
     * @return region 地区
     */
    private String buildRegion() {
        // 阿里云必须有 region，否则会报错
        if (config.getEndpoint().contains(ENDPOINT_ALIYUN)) {
            return config.getEndpoint().substring(config.getEndpoint().lastIndexOf('.') + 1)
                    .replaceAll("-internal", "") // 去除内网 Endpoint 的后缀
                    .replaceAll("https://", "");
        }
        // 腾讯云必须有 region，否则会报错
        if (config.getEndpoint().contains(ENDPOINT_TENCENT)) {
            return config.getEndpoint().substring(config.getEndpoint().indexOf("cos.") + 4, config.getEndpoint().lastIndexOf('.'))
                    .replaceAll("." + ENDPOINT_TENCENT, ""); // 去除 Endpoint
        }
        return null;
    }

    @Override
    public String upload(byte[] content, String path, String type) {
        // 执行上传
        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentType(type);
        // set object metadata...
        client.putObject(config.getBucket(), path, new ByteArrayInputStream(content), objectMetadata);
        // 拼接返回路径
        return config.getDomain() + "/" + path;
    }

    @Override
    public void delete(String path) {
        client.deleteObject(config.getBucket(), path);
    }

    @Override
    public byte[] getContent(String path) throws Exception {
        return client.getObject(config.getBucket(), path).getObjectContent().readAllBytes();
    }

}
