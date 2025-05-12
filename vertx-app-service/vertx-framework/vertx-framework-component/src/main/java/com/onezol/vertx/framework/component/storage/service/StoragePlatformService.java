package com.onezol.vertx.framework.component.storage.service;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.onezol.vertx.framework.common.exception.InvalidParameterException;
import com.onezol.vertx.framework.common.skeleton.service.BaseServiceImpl;
import com.onezol.vertx.framework.common.util.BeanUtils;
import com.onezol.vertx.framework.common.util.SpringWebUtils;
import com.onezol.vertx.framework.component.storage.annotation.StorageType;
import com.onezol.vertx.framework.component.storage.mapper.StorageMapper;
import com.onezol.vertx.framework.component.storage.model.dto.StoragePlatform;
import com.onezol.vertx.framework.component.storage.model.entity.StoragePlatformEntity;
import com.onezol.vertx.framework.component.storage.model.input.StoragePayload;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.dromara.x.file.storage.core.FileStorageProperties;
import org.dromara.x.file.storage.core.FileStorageService;
import org.dromara.x.file.storage.core.FileStorageServiceBuilder;
import org.dromara.x.file.storage.core.platform.FileStorage;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.CopyOnWriteArrayList;

import static com.onezol.vertx.framework.component.storage.helper.StorageHelper.fixDomain;
import static com.onezol.vertx.framework.component.storage.helper.StorageHelper.getLocalFileApiPrefix;

@Slf4j
@Service
public class StoragePlatformService extends BaseServiceImpl<StorageMapper, StoragePlatformEntity> {

    private final FileStorageService fileStorageService;

    public StoragePlatformService(FileStorageService fileStorageService) {
        this.fileStorageService = fileStorageService;
    }

    @PostConstruct
    private void initStorageStrategy() {
        StoragePlatform storage = this.getDefaultStoragePlatform();
        if (storage == null) {
            log.warn("[StorageService] 未配置默认存储平台，文件存储服务不可用");
            return;
        }
        this.load(storage);
    }

    /**
     * 获取存储平台列表
     */
    public List<StoragePlatform> listPlatforms() {
        List<StoragePlatformEntity> entities = this.list();
        return BeanUtils.toList(entities, StoragePlatform.class);
    }

    /**
     * 更新默认存储平台
     *
     * @param id 存储平台ID
     */
    @Transactional
    public void updateDefaultStorage(Long id) {
        List<StoragePlatformEntity> entities = this.list();

        StoragePlatformEntity shouldLoadPlatformEntity = null;
        StoragePlatformEntity shouldUnloadPlatformEntity = null;
        for (StoragePlatformEntity entity : entities) {
            if (!entity.getId().equals(id) && entity.getIsDefault()) {
                entity.setIsDefault(false);
                shouldUnloadPlatformEntity = entity;
            }
            if (entity.getId().equals(id)) {
                entity.setIsDefault(true);
                shouldLoadPlatformEntity = entity;
            } else {
                entity.setIsDefault(false);
            }
        }

        if (shouldLoadPlatformEntity == null) {
            throw new InvalidParameterException("未找到指定存储平台");
        }

        this.updateBatchById(entities);

        StoragePlatform shouldLoadPlatform = BeanUtils.toBean(shouldLoadPlatformEntity, StoragePlatform.class);
        StoragePlatform shouldUnloadPlatform = BeanUtils.toBean(shouldUnloadPlatformEntity, StoragePlatform.class);
        this.unload(shouldUnloadPlatform);
        this.load(shouldLoadPlatform);
    }

    /**
     * 根据ID获取详情
     */
    public StoragePlatform getStoragePlatform(Long id) {
        StoragePlatformEntity entity = this.getById(id);
        return BeanUtils.toBean(entity, StoragePlatform.class);
    }

    /**
     * 获取默认存储平台
     */
    public StoragePlatform getDefaultStoragePlatform() {
        StoragePlatformEntity entity = this.getOne(Wrappers.<StoragePlatformEntity>lambdaQuery().eq(StoragePlatformEntity::getIsDefault, true));
        return BeanUtils.toBean(entity, StoragePlatform.class);
    }

    /**
     * 创建存储平台
     */
    public StoragePlatform createStoragePlatform(StoragePayload payload) {
        StoragePlatformEntity entity = BeanUtils.toBean(payload, StoragePlatformEntity.class);
        this.save(entity);
        return this.getStoragePlatform(entity.getId());
    }

    /**
     * 更新存储平台
     */
    public StoragePlatform updateStoragePlatform(StoragePayload payload) {
        StoragePlatformEntity platformEntity = this.getById(payload.getId());
        if (payload.getId() == null || Objects.isNull(platformEntity)) {
            throw new InvalidParameterException("未找到指定存储平台");
        }
        StoragePlatformEntity shouldUpdateEntity = BeanUtils.toBean(payload, StoragePlatformEntity.class);
        this.updateById(shouldUpdateEntity);

        StoragePlatform storagePlatform = this.getStoragePlatform(payload.getId());
        if (platformEntity.getIsDefault()) {
            this.unload(storagePlatform);
            this.load(storagePlatform);
        }

        return storagePlatform;
    }

    /**
     * 加载存储平台
     *
     * @param platform 存储平台
     */
    private void load(StoragePlatform platform) {
        CopyOnWriteArrayList<FileStorage> fileStorages = fileStorageService.getFileStorageList();
        if (StorageType.LOCAL.equals(platform.getType())) {
            FileStorageProperties.LocalPlusConfig config = new FileStorageProperties.LocalPlusConfig();
            config.setPlatform(platform.getCode());
            config.setStoragePath(platform.getBucketName());
            config.setDomain(fixDomain(platform.getDomain()));
            fileStorages.addAll(
                    FileStorageServiceBuilder.buildLocalPlusFileStorage(Collections.singletonList(config))
            );
            SpringWebUtils.registerResourceHandler(
                    Map.of(getLocalFileApiPrefix(platform.getDomain()), platform.getBucketName())
            );
        } else if (StorageType.OSS.equals(platform.getType())) {
            FileStorageProperties.AmazonS3Config config = new FileStorageProperties.AmazonS3Config();
            config.setPlatform(platform.getCode());
            config.setAccessKey(platform.getAccessKey());
            config.setSecretKey(platform.getSecretKey());
            config.setEndPoint(platform.getEndpoint());
            config.setBucketName(platform.getBucketName());
            config.setDomain(fixDomain(platform.getDomain()));
            fileStorages.addAll(
                    FileStorageServiceBuilder.buildAmazonS3FileStorage(Collections.singletonList(config), null)
            );
        }
        log.info("[StorageService] 加载存储平台 {} 成功", platform.getCode());
    }

    /**
     * 卸载存储平台
     */
    private void unload(StoragePlatform platform) {
        if (Objects.isNull(platform)) {
            return;
        }
        CopyOnWriteArrayList<FileStorage> fileStorages = fileStorageService.getFileStorageList();
        FileStorage fileStorage = fileStorageService.getFileStorage(platform.getCode());
        fileStorages.remove(fileStorage);
        fileStorage.close();
        if (StorageType.LOCAL.equals(platform.getType())) {
            SpringWebUtils.deRegisterResourceHandler(
                    Map.of(getLocalFileApiPrefix(platform.getDomain()), platform.getBucketName())
            );
        }
        log.info("[StorageService] 卸载存储平台 {} 成功", platform.getCode());
    }

}