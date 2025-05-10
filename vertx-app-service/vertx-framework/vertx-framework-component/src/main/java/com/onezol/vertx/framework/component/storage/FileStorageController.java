package com.onezol.vertx.framework.component.storage;

import com.onezol.vertx.framework.common.model.GenericResponse;
import com.onezol.vertx.framework.component.storage.service.FileUploadService;
import com.onezol.vertx.framework.support.support.ResponseHelper;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.dromara.x.file.storage.core.FileInfo;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@Tag(name = "文件存储")
@RestController
@RequestMapping("/file-storage")
public class FileStorageController {

    private final FileUploadService fileUploadService;

    public FileStorageController(FileUploadService fileUploadService) {
        this.fileUploadService = fileUploadService;
    }

    @RequestMapping("/upload")
    public GenericResponse<String> upload(@RequestParam("file") MultipartFile file) {
        FileInfo fileInfo = fileUploadService.upload(file);
        return ResponseHelper.buildSuccessfulResponse(fileInfo.getUrl());
    }

}
