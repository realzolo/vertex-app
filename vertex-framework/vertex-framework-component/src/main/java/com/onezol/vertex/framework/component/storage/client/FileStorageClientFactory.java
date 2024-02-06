package com.onezol.vertex.framework.component.storage.client;

import com.onezol.vertex.framework.component.storage.constant.FileStorage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.Assert;

import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * 文件客户端的工厂实现类
 */
@Slf4j
public final class FileStorageClientFactory {

    /**
     * 文件客户端 Map
     */
    private final static ConcurrentMap<String, AbstractFileStorageClient<?>> clients = new ConcurrentHashMap<>();

    public static FileStorageClient getFileStorageClient(String storage) {
        AbstractFileStorageClient<?> client = clients.get(storage);
        if (client == null) {
            log.error("未找到文件存储客户端({}), 请检查相关配置！", storage);
        }
        return client;
    }

    @SuppressWarnings("unchecked")
    public static <Config extends FileStorageClientConfig> void createOrUpdateFileStorageClient(String storage, Config config) {
        AbstractFileStorageClient<Config> client = (AbstractFileStorageClient<Config>) clients.get(storage);
        if (Objects.isNull(client)) {
            client = createFileStorageClient(storage, config);
            client.init();
            clients.put(storage, client);
            log.info("{} 文件存储客户端初始化成功", storage);
        } else {
            client.refresh(config);
            log.info("{} 文件存储客户端更新成功", storage);
        }
    }

    @SuppressWarnings("ALL")
    private static <Config extends FileStorageClientConfig> AbstractFileStorageClient<Config> createFileStorageClient(String storage, Config config) {
        FileStorage storageEnum = FileStorage.getByStorage(storage);
        Assert.notNull(storageEnum, String.format("文件配置(%s) 为空", storageEnum));
        // 创建客户端
        try {
            return (AbstractFileStorageClient<Config>) storageEnum.getClientClass().getConstructor(getClasses(config)).newInstance(config);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static Class<?>[] getClasses(Object... objects) {
        Class<?>[] classes = new Class<?>[objects.length];
        for (int i = 0; i < objects.length; i++) {
            classes[i] = objects[i].getClass();
        }
        return classes;
    }
}
