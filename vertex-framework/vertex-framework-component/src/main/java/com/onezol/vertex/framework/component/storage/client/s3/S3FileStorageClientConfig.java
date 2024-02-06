package com.onezol.vertex.framework.component.storage.client.s3;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.onezol.vertex.framework.common.util.StringUtils;
import com.onezol.vertex.framework.component.storage.client.FileStorageClientConfig;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.URL;

/**
 * S3 文件客户端的配置类
 */
@Data
@SuppressWarnings("ALL")
public class S3FileStorageClientConfig implements FileStorageClientConfig {

    public static final String ENDPOINT_QINIU = "qiniucs.com";
    public static final String ENDPOINT_ALIYUN = "aliyuncs.com";
    public static final String ENDPOINT_TENCENT = "myqcloud.com";

    /**
     * 访问 Key
     * 1. MinIO：<a href="https://www.iocoder.cn/Spring-Boot/MinIO">https://www.iocoder.cn/Spring-Boot/MinIO</a> <br/>
     * 2. 阿里云：<a href="https://ram.console.aliyun.com/manage/ak">https://ram.console.aliyun.com/manage/ak</a> <br/>
     * 3. 腾讯云：<a href="https://console.cloud.tencent.com/cam/capi">https://console.cloud.tencent.com/cam/capi</a> <br/>
     * 4. 七牛云：<a href="https://portal.qiniu.com/user/key">https://portal.qiniu.com/user/key</a> <br/>
     * 5. 华为云：<a href="https://support.huaweicloud.com/qs-obs/obs_qs_0005.html">https://support.huaweicloud.com/qs-obs/obs_qs_0005.html</a> <br/>
     */
    @NotNull(message = "accessKey 不能为空")
    private String accessKey;

    /**
     * 访问 Secret
     */
    @NotNull(message = "accessSecret 不能为空")
    private String accessSecret;

    /**
     * 节点地址
     * 1. MinIO：<a href="https://www.iocoder.cn/Spring-Boot/MinIO">https://www.iocoder.cn/Spring-Boot/MinIO</a> <br/>
     * 2. 阿里云：<a href="https://help.aliyun.com/document_detail/31837.html">https://help.aliyun.com/document_detail/31837.html</a> <br/>
     * 3. 腾讯云：<a href="https://cloud.tencent.com/document/product/436/6224">https://cloud.tencent.com/document/product/436/6224</a> <br/>
     * 4. 七牛云：<a href="https://developer.qiniu.com/kodo/4088/s3-access-domainname">https://developer.qiniu.com/kodo/4088/s3-access-domainname</a> <br/>
     * 5. 华为云：<a href="https://developer.huaweicloud.com/endpoint?OBS">https://developer.huaweicloud.com/endpoint?OBS</a> <br/>
     */
    @NotNull(message = "endpoint 不能为空")
    private String endpoint;

    /**
     * 存储 Bucket
     */
    @NotNull(message = "bucket 不能为空")
    private String bucket;

    /**
     * 自定义域名
     * 1. MinIO：通过 Nginx 配置
     * 2. 阿里云：<a href="https://help.aliyun.com/document_detail/31836.html">https://help.aliyun.com/document_detail/31836.html</a> <br/>
     * 3. 腾讯云：<a href="https://cloud.tencent.com/document/product/436/11142">https://cloud.tencent.com/document/product/436/11142</a> <br/>
     * 4. 七牛云：<a href="https://developer.qiniu.com/kodo/8556/set-the-custom-source-domain-name">https://developer.qiniu.com/kodo/8556/set-the-custom-source-domain-name</a> <br/>
     * 5. 华为云：<a href="https://support.huaweicloud.com/usermanual-obs/obs_03_0032.html">https://support.huaweicloud.com/usermanual-obs/obs_03_0032.html</a> <br/>
     */
    @URL(message = "domain 必须是 URL 格式")
    private String domain;

    @SuppressWarnings("RedundantIfStatement")
    @AssertTrue(message = "domain 不能为空")
    @JsonIgnore
    public boolean isDomainValid() {
        // 如果是七牛，必须带有 domain
        if (StringUtils.isNotBlank(endpoint) && endpoint.contains(ENDPOINT_QINIU) && StringUtils.isBlank(domain)) {
            return false;
        }
        return true;
    }

}
