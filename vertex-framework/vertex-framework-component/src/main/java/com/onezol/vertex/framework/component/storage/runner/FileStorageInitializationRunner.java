package com.onezol.vertex.framework.component.storage.runner;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.onezol.vertex.framework.common.model.entity.RuntimeConfigurationEntity;
import com.onezol.vertex.framework.component.storage.client.FileStorageClientFactory;
import com.onezol.vertex.framework.component.storage.client.s3.S3FileStorageClientConfig;
import com.onezol.vertex.framework.component.storage.constant.FileStorage;
import com.onezol.vertex.framework.support.mapper.RuntimeConfigurationMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Component
public class FileStorageInitializationRunner implements ApplicationRunner {

    public static final String FILE_STORAGE_CONFIGURATION_KEY = "FileStorage";

    private final RuntimeConfigurationMapper runtimeConfigurationMapper;

    public FileStorageInitializationRunner(RuntimeConfigurationMapper runtimeConfigurationMapper) {
        this.runtimeConfigurationMapper = runtimeConfigurationMapper;
    }

    @Override
    public void run(ApplicationArguments args) {
        // 获取所有的文件存储配置
        List<RuntimeConfigurationEntity> fileStorageConfigs = runtimeConfigurationMapper.selectList(
                Wrappers.<RuntimeConfigurationEntity>lambdaQuery()
                        .in(RuntimeConfigurationEntity::getSubject, FILE_STORAGE_CONFIGURATION_KEY)
        );

        // 按照configName进行分组
        Map<String, List<RuntimeConfigurationEntity>> configMap = fileStorageConfigs.stream().collect(Collectors.groupingBy(RuntimeConfigurationEntity::getConfigName));

        // 初始化文件存储客户端
        initFileStorageClients(configMap);
    }

    private void initFileStorageClients(Map<String, List<RuntimeConfigurationEntity>> configMap) {
        if (Objects.isNull(configMap) || configMap.isEmpty()) {
            log.warn("未找到文件存储配置项，无法初始化客户端, 将导致文件存储功能不可用！");
            return;
        } else {
            log.info("发现文件存储配置项({})，正在初始化文件存储客户端...", String.join(",", configMap.keySet()));
        }

        for (String configName : configMap.keySet()) {
            List<RuntimeConfigurationEntity> fileStorageConfigs = configMap.get(configName);
            if (Objects.isNull(fileStorageConfigs) || fileStorageConfigs.isEmpty()) {
                log.warn("文件存储配置项({})为空，无法初始化文件存储客户端", configName);
                continue;
            }
            if (FileStorage.AmazonS3.getStorage().equals(configName)) {
                S3FileStorageClientConfig s3FileStorageClientConfig = initS3FileStorageClientConfig(fileStorageConfigs);
                FileStorageClientFactory.createOrUpdateFileStorageClient(configName, s3FileStorageClientConfig);
            }
        }
    }

    /**
     * 初始化AmazonS3客户端
     *
     * @param fileStorageConfigs 文件存储配置
     * @return AmazonS3客户端
     */
    private S3FileStorageClientConfig initS3FileStorageClientConfig(List<RuntimeConfigurationEntity> fileStorageConfigs) {
        if (Objects.isNull(fileStorageConfigs) || fileStorageConfigs.isEmpty()) {
            log.warn("Amazon S3 文件存储配置为空, 无法初始化 S3 文件存储客户端");
            return null;
        }
        S3FileStorageClientConfig config = new S3FileStorageClientConfig();
        fileStorageConfigs.forEach(fileStorageConfig -> {
            String value = fileStorageConfig.getConfigValue();
            switch (fileStorageConfig.getConfigKey()) {
                case "accessKey":
                    config.setAccessKey(value);
                    break;
                case "accessSecret":
                    config.setAccessSecret(value);
                    break;
                case "endpoint":
                    config.setEndpoint(value);
                    break;
                case "bucket":
                    config.setBucket(value);
                    break;
                case "domain":
                    config.setDomain(value);
                    break;
                default:
                    log.warn("[S3FileStorageClientConfig][配置({}) 无需处理]", fileStorageConfig);
                    break;
            }
        });
        return config;
    }

}
