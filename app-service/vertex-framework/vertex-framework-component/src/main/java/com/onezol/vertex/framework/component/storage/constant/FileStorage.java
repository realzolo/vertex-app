package com.onezol.vertex.framework.component.storage.constant;

import com.onezol.vertex.framework.component.storage.client.FileStorageClient;
import com.onezol.vertex.framework.component.storage.client.FileStorageClientConfig;
import com.onezol.vertex.framework.component.storage.client.s3.S3FileStorageClient;
import com.onezol.vertex.framework.component.storage.client.s3.S3FileStorageClientConfig;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 文件存储器枚举
 */
@AllArgsConstructor
@Getter
public enum FileStorage {

    AmazonS3("AmazonS3", S3FileStorageClientConfig.class, S3FileStorageClient.class),
    ;

    /**
     * 存储器
     */
    private final String storage;

    /**
     * 配置类
     */
    private final Class<? extends FileStorageClientConfig> configClass;
    /**
     * 客户端类
     */
    private final Class<? extends FileStorageClient> clientClass;

    public static FileStorage getByStorage(String storage) {
        for (FileStorage value : values()) {
            if (value.getStorage().equals(storage)) {
                return value;
            }
        }
        return null;
    }

}
