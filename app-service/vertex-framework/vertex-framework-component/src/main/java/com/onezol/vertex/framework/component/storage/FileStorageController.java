package com.onezol.vertex.framework.component.storage;

import com.onezol.vertex.framework.common.helper.ResponseHelper;
import com.onezol.vertex.framework.common.model.GenericResponse;
import com.onezol.vertex.framework.component.storage.client.FileStorageClient;
import com.onezol.vertex.framework.component.storage.client.FileStorageClientFactory;
import com.onezol.vertex.framework.component.storage.constant.FileStorage;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Schema(name = "FileStorageController", description = "文件存储")
@RestController
@RequestMapping("/file-storage")
public class FileStorageController {

    @RequestMapping("/test")
    public GenericResponse<Object> test() {
        FileStorageClient fileStorageClient = FileStorageClientFactory.getFileStorageClient(FileStorage.AmazonS3.getStorage());
        return ResponseHelper.buildSuccessfulResponse();
    }

}
