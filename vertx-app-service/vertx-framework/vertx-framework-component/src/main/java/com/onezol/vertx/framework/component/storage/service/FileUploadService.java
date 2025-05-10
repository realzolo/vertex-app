package com.onezol.vertx.framework.component.storage.service;

import com.onezol.vertx.framework.common.constant.StringConstants;
import com.onezol.vertx.framework.common.constant.enumeration.FileType;
import com.onezol.vertx.framework.common.exception.InvalidParameterException;
import com.onezol.vertx.framework.common.exception.RuntimeServiceException;
import com.onezol.vertx.framework.component.storage.model.dto.StoragePlatform;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.dromara.x.file.storage.core.FileInfo;
import org.dromara.x.file.storage.core.FileStorageService;
import org.dromara.x.file.storage.core.ProgressListener;
import org.dromara.x.file.storage.core.upload.UploadPretreatment;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Objects;

import static com.onezol.vertx.framework.component.storage.helper.StorageHelper.generateFileName;
import static com.onezol.vertx.framework.component.storage.helper.StorageHelper.generateFilePath;

@Slf4j
@Service
public class FileUploadService {

    private final FileStorageService fileStorageService;

    private final StoragePlatformService storagePlatformService;

    private final FIleRecorderService fileRecorderService;

    public FileUploadService(FileStorageService fileStorageService, StoragePlatformService storagePlatformService, FIleRecorderService fileRecorderService) {
        this.fileStorageService = fileStorageService;
        this.storagePlatformService = storagePlatformService;
        this.fileRecorderService = fileRecorderService;
    }

    /**
     * 上传文件
     *
     * @param file 上传文件
     */
    public FileInfo upload(MultipartFile file) {
        if (Objects.isNull(file)) {
            throw new InvalidParameterException("上传文件不可为空");
        }

        String extName = FilenameUtils.getExtension(file.getOriginalFilename());
        String extSuffix = StringConstants.DOT + extName;
        String filePath = generateFilePath();
        String fileName = generateFileName();

        log.info("[FileUploadService] 文件上传开始，文件名：{}", fileName);

        StoragePlatform platform = storagePlatformService.getDefaultStoragePlatform();
        UploadPretreatment uploadPretreatment = fileStorageService.of(file)
                .setPlatform(platform.getCode())
                .setPath(filePath)
                .setSaveFilename(fileName + extSuffix)
                .setSaveThFilename(fileName)
                .putAttr("platform", platform);
        if (FileType.IMAGE.getExtensions().contains(extName)) {
            uploadPretreatment.thumbnail(image -> image.size(128, 128));
        }
        uploadPretreatment.setProgressMonitor(this.buildProgressListener());

        try {
            return uploadPretreatment.upload();
        } catch (Exception e) {
            log.error("[FileUploadService] 文件上传失败！", e);
            throw new RuntimeServiceException("文件上传失败！");
        }
    }


    /**
     * 删除文件
     *
     * @param url 文件访问路径
     */
    public void delete(String url) {
        StoragePlatform platform = storagePlatformService.getDefaultStoragePlatform();
        FileInfo fileInfo = fileRecorderService.getByUrl(url);
        if (Objects.isNull(fileInfo)) {
            log.warn("[FileUploadService] 文件记录不存在，无法删除，文件路径：{}", url);
            return;
        }
        if (!Objects.equals(platform.getCode(), fileInfo.getPlatform())) {
            log.warn("[FileUploadService] 文件存储平台不一致，无法删除，文件路径：{}", url);
            return;
        }
        if (!fileStorageService.exists(url)) {
            log.warn("[FileUploadService] 文件不存在，无法删除，文件路径：{}", url);
        }
        fileStorageService.delete(url);
        log.info("[FileUploadService] 文件删除成功，文件路径：{}", url);
    }

    /**
     * 进度监听器
     */
    private ProgressListener buildProgressListener() {
        return new ProgressListener() {
            @Override
            public void start() {
                log.info("[FileUploadService] 开始上传");
            }

            @Override
            public void progress(long progressSize, Long allSize) {
                log.info("[FileUploadService] 已上传 [{}]，总大小 [{}]", progressSize, allSize);
            }

            @Override
            public void finish() {
                log.info("[FileUploadService] 上传结束");
            }
        };
    }

}
