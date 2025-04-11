package com.onezol.vertex.framework.component.storage.service;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.onezol.vertex.framework.common.constant.StringConstants;
import com.onezol.vertex.framework.common.constant.enumeration.ServiceStatusEnum;
import com.onezol.vertex.framework.common.constant.enumeration.FileTypeEnum;
import com.onezol.vertex.framework.common.exception.RuntimeServiceException;
import com.onezol.vertex.framework.component.storage.annotation.StorageTypeEnum;
import com.onezol.vertex.framework.component.storage.mapper.FileRecordMapper;
import com.onezol.vertex.framework.component.storage.mapper.StorageStrategyMapper;
import com.onezol.vertex.framework.component.storage.model.FileRecordEntity;
import com.onezol.vertex.framework.component.storage.model.StorageStrategyEntity;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.dromara.x.file.storage.core.FileInfo;
import org.dromara.x.file.storage.core.FileStorageProperties;
import org.dromara.x.file.storage.core.FileStorageServiceBuilder;
import org.dromara.x.file.storage.core.ProgressListener;
import org.dromara.x.file.storage.core.platform.FileStorage;
import org.dromara.x.file.storage.core.upload.FilePartInfo;
import org.dromara.x.file.storage.core.upload.UploadPretreatment;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collections;
import java.util.Objects;
import java.util.concurrent.CopyOnWriteArrayList;

import static com.onezol.vertex.framework.component.storage.helper.StorageHelper.*;

@Slf4j
@Service("frameworkFileStorageService")
public class FileStorageService {

    private final org.dromara.x.file.storage.core.FileStorageService fileStorageService;
    private final StorageStrategyMapper storageStrategyMapper;
    private StorageStrategyEntity storageStrategy;

    public FileStorageService(org.dromara.x.file.storage.core.FileStorageService fileStorageService, StorageStrategyMapper storageStrategyMapper) {
        this.fileStorageService = fileStorageService;
        this.storageStrategyMapper = storageStrategyMapper;
    }

    @PostConstruct
    private void initStorageStrategy() {
        StorageStrategyEntity strategy = storageStrategyMapper.selectOne(
                Wrappers.<StorageStrategyEntity>lambdaQuery()
                        .eq(StorageStrategyEntity::getIsDefault, true)
        );
        if (strategy == null) {
            log.warn("未配置默认存储策略，文件存储服务不可用");
            return;
        }
        storageStrategy = this.loadStrategy(strategy);
    }

    /**
     * 上传文件
     *
     * @param file 上传文件
     */
    public String upload(MultipartFile file) {
        if (Objects.isNull(file)) {
            throw new IllegalArgumentException("上传文件不可为空");
        }
        if (Objects.isNull(storageStrategy)) {
            throw new RuntimeServiceException("未配置默认存储策略，文件存储服务不可用");
        }

        String extName = FilenameUtils.getExtension(file.getOriginalFilename());
        String extSuffix = StringConstants.DOT + extName;
        String filePath = generateFilePath();
        String fileName = generateFileName();

        log.info("文件上传开始，文件名：{}", fileName);
        UploadPretreatment uploadPretreatment = fileStorageService.of(file)
                .setPlatform(storageStrategy.getCode())
                .setPath(filePath)
                .setSaveFilename(fileName + extSuffix)
                .setSaveThFilename(fileName)
                .putAttr("storageStrategy", storageStrategy);
        if (FileTypeEnum.IMAGE.getExtensions().contains(extName)) {
            uploadPretreatment.thumbnail(image -> image.size(128, 128));
        }
        uploadPretreatment.setProgressMonitor(this.buildProgressListener());

        try {
            uploadPretreatment.upload();
        } catch (Exception e) {
            throw new RuntimeServiceException(ServiceStatusEnum.INTERNAL_SERVER_ERROR, "文件上传失败");
        }

        if (Objects.equals(storageStrategy.getType(), StorageTypeEnum.S3)) {
            return String.format("%s/%s%s%s%s%s", fixDomain(storageStrategy.getDomain()), storageStrategy.getBucketName(), fixRootPath(storageStrategy.getRootPath()), filePath, fileName, extSuffix);
        }
        return String.format("%s%s%s%s", fixDomain(storageStrategy.getDomain()), filePath, fileName, extSuffix);
    }


    /**
     * 更新默认存储策略
     *
     * @param storageCode 存储策略编码
     */
    public void updateDefaultStorageStrategy(String storageCode) {
        StorageStrategyEntity entity = storageStrategyMapper.selectOne(
                Wrappers.<StorageStrategyEntity>lambdaQuery()
                        .eq(StorageStrategyEntity::getCode, storageCode)
        );
        if (entity == null) {
            throw new RuntimeException("未找到存储策略");
        }
        entity.setIsDefault(true);
        storageStrategyMapper.updateById(entity);
        this.storageStrategy = entity;
    }

    /**
     * 加载存储策略
     * @param strategy 存储策略
     */
    private StorageStrategyEntity loadStrategy(StorageStrategyEntity strategy) {
        CopyOnWriteArrayList<FileStorage> fileStorageList = fileStorageService.getFileStorageList();
        if (StorageTypeEnum.LOCAL.equals(strategy.getType())) {
            FileStorageProperties.LocalPlusConfig config = new FileStorageProperties.LocalPlusConfig();
            config.setPlatform(strategy.getCode());
            config.setStoragePath(fixRootPath(strategy.getRootPath()));
            config.setDomain(fixDomain(strategy.getDomain()));
            fileStorageList.addAll(
                    FileStorageServiceBuilder.buildLocalPlusFileStorage(Collections.singletonList(config))
            );
        } else if (StorageTypeEnum.S3.equals(strategy.getType())) {
            FileStorageProperties.AmazonS3Config config = new FileStorageProperties.AmazonS3Config();
            config.setPlatform(strategy.getCode());
            config.setAccessKey(strategy.getAccessKey());
            config.setSecretKey(strategy.getSecretKey());
            config.setEndPoint(strategy.getEndpoint());
            config.setBucketName(strategy.getBucketName());
            config.setBasePath(fixRootPath(strategy.getRootPath()));
            config.setDomain(fixDomain(strategy.getDomain()));
            fileStorageList.addAll(
                    FileStorageServiceBuilder.buildAmazonS3FileStorage(Collections.singletonList(config), null)
            );
        }
        return strategy;
    }

    /**
     * 构建进度监听器
     *
     * @return ProgressListener
     */
    private ProgressListener buildProgressListener() {
        return new ProgressListener() {
            @Override
            public void start() {
                log.info("开始上传");
            }

            @Override
            public void progress(long progressSize, Long allSize) {
                log.info("已上传 [{}]，总大小 [{}]", progressSize, allSize);
            }

            @Override
            public void finish() {
                log.info("上传结束");
            }
        };
    }

    @Component
    public static class FIleRecorder implements org.dromara.x.file.storage.core.recorder.FileRecorder {

        private final FileRecordMapper fileRecordMapper;
        private final StorageStrategyMapper storageStrategyMapper;

        public FIleRecorder(FileRecordMapper fileRecordMapper, StorageStrategyMapper storageStrategyMapper) {
            this.fileRecordMapper = fileRecordMapper;
            this.storageStrategyMapper = storageStrategyMapper;
        }

        /**
         * 保存文件记录
         */
        @Override
        public boolean save(FileInfo fileInfo) {
            FileRecordEntity fileRecord = new FileRecordEntity();
            fileRecord.setName(fileInfo.getOriginalFilename());
            fileRecord.setUrl(fileInfo.getUrl());
            fileRecord.setSize(fileInfo.getSize());
            fileRecord.setExtension(fileInfo.getExt());
            fileRecord.setType(FileTypeEnum.getByExtension(fileRecord.getExtension()));
            fileRecord.setThumbnailUrl(fileInfo.getThUrl());
            fileRecord.setThumbnailSize(fileInfo.getThSize());
            StorageStrategyEntity storageStrategy = (StorageStrategyEntity) fileInfo.getAttr().get("storageStrategy");
            fileRecord.setStorageStrategyId(storageStrategy.getId());
            int affectedRows = fileRecordMapper.insert(fileRecord);
            return affectedRows > 0;
        }

        /**
         * 根据 url 获取文件记录
         */
        @Override
        public FileInfo getByUrl(String url) {
            FileRecordEntity fileRecord = fileRecordMapper.selectOne(
                    Wrappers.<FileRecordEntity>lambdaQuery()
                            .eq(FileRecordEntity::getUrl, url)
            );
            StorageStrategyEntity storageStrategy = storageStrategyMapper.selectOne(
                    Wrappers.<StorageStrategyEntity>lambdaQuery()
                            .eq(StorageStrategyEntity::getId, fileRecord.getStorageStrategyId())
            );
            return fileRecord.toFileInfo(storageStrategy.getCode());
        }

        /**
         * 根据 url 删除文件记录
         */
        @Override
        public boolean delete(String url) {
            int affectedRows = fileRecordMapper.delete(
                    Wrappers.<FileRecordEntity>lambdaQuery()
                            .eq(FileRecordEntity::getUrl, url)
            );
            return affectedRows > 0;
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
    }
}