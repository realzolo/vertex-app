package com.onezol.vertx.framework.component.storage;

import com.onezol.vertx.framework.support.support.ResponseHelper;
import com.onezol.vertx.framework.common.model.GenericResponse;
import com.onezol.vertx.framework.component.storage.service.FileStorageService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@Tag(name = "文件存储")
@RestController
@RequestMapping("/file-storage")
public class FileStorageController {

    @Autowired
    private FileStorageService fileStorageService;

    @RequestMapping("/upload")
    public GenericResponse<String> upload(@RequestParam("file") MultipartFile file) {
        String url = fileStorageService.upload(file);
        if (url == null) {
            return ResponseHelper.buildFailedResponse("文件上传失败");
        }
        return ResponseHelper.buildSuccessfulResponse(url);
    }

}
