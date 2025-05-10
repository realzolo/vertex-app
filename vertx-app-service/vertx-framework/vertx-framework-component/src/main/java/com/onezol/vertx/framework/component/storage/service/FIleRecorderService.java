package com.onezol.vertx.framework.component.storage.service;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.onezol.vertx.framework.common.constant.enumeration.FileType;
import com.onezol.vertx.framework.common.skeleton.service.BaseServiceImpl;
import com.onezol.vertx.framework.component.storage.helper.StorageHelper;
import com.onezol.vertx.framework.component.storage.mapper.FileRecordMapper;
import com.onezol.vertx.framework.component.storage.model.dto.StoragePlatform;
import com.onezol.vertx.framework.component.storage.model.entity.FileRecordEntity;
import com.onezol.vertx.framework.component.storage.model.entity.StoragePlatformEntity;
import lombok.extern.slf4j.Slf4j;
import org.dromara.x.file.storage.core.FileInfo;
import org.dromara.x.file.storage.core.upload.FilePartInfo;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Slf4j
@Service
public class FIleRecorderService extends BaseServiceImpl<FileRecordMapper, FileRecordEntity> implements org.dromara.x.file.storage.core.recorder.FileRecorder {

    private final StoragePlatformService storagePlatformService;

    public FIleRecorderService(@Lazy StoragePlatformService storagePlatformService) {
        this.storagePlatformService = storagePlatformService;
    }

    /**
     * 保存文件记录
     */
    @Override
    public boolean save(FileInfo fileInfo) {
        StoragePlatform platform = (StoragePlatform) fileInfo.getAttr().get("platform");

        FileRecordEntity fileRecord = new FileRecordEntity();
        fileRecord.setName(fileInfo.getFilename());
        fileRecord.setOriginalName(fileInfo.getOriginalFilename());
        fileRecord.setPath(fileInfo.getPath());
        fileRecord.setUrl(fileInfo.getUrl());
        fileRecord.setSize(fileInfo.getSize());
        fileRecord.setThumbnailName(StorageHelper.getThumbnailName(fileInfo.getThUrl()));
        fileRecord.setThumbnailUrl(fileInfo.getThUrl());
        fileRecord.setThumbnailSize(fileInfo.getThSize());
        fileRecord.setExtension(fileInfo.getExt());
        fileRecord.setType(FileType.getByExtension(fileRecord.getExtension()));
        fileRecord.setStoragePlatformId(platform.getId());

        return this.save(fileRecord);
    }

    /**
     * 根据 url 获取文件记录
     */
    @Override
    public FileInfo getByUrl(String url) {
        FileRecordEntity fileRecord = this.getRecordByUrl(url);
        if (Objects.isNull(fileRecord)) {
            log.error("[FIleRecorderService] 文件记录不存在");
            return null;
        }
        StoragePlatform platform = storagePlatformService.getStoragePlatform(fileRecord.getStoragePlatformId());
        return fileRecord.toFileInfo(platform.getCode());
    }

    /**
     * 根据 url 删除文件记录
     */
    @Override
    public boolean delete(String url) {
        return this.remove(
                Wrappers.<FileRecordEntity>lambdaQuery().eq(FileRecordEntity::getUrl, url)
        );
    }

    /**
     * 更新文件记录，可以根据文件 ID 或 URL 来更新文件记录，
     * 主要用在手动分片上传文件-完成上传，作用是更新文件信息
     */
    @Override
    public void update(FileInfo fileInfo) {
    }

    /**
     * 保存文件分片信息
     *
     * @param filePartInfo 文件分片信息
     */
    @Override
    public void saveFilePart(FilePartInfo filePartInfo) {
    }

    /**
     * 删除文件分片信息
     */
    @Override
    public void deleteFilePartByUploadId(String uploadId) {
    }

    private FileRecordEntity getRecordByUrl(String url) {
        return this.getOne(
                Wrappers.<FileRecordEntity>lambdaQuery()
                        .eq(FileRecordEntity::getUrl, url)
        );
    }
}