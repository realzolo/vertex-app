package com.onezol.vertx.framework.component.storage.controller;

import com.onezol.vertx.framework.common.model.GenericResponse;
import com.onezol.vertx.framework.component.storage.model.dto.StoragePlatform;
import com.onezol.vertx.framework.component.storage.model.input.StoragePayload;
import com.onezol.vertx.framework.component.storage.service.StoragePlatformService;
import com.onezol.vertx.framework.support.support.ResponseHelper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "存储平台")
@RestController
@RequestMapping("/storage/platform")
public class StoragePlatformController {

    private final StoragePlatformService storagePlatformService;

    public StoragePlatformController(StoragePlatformService storagePlatformService) {
        this.storagePlatformService = storagePlatformService;
    }

    @Operation(summary = "获取列表", description = "获取存储平台列表")
    @GetMapping("/list")
    public GenericResponse<List<StoragePlatform>> list() {
        List<StoragePlatform> platforms = storagePlatformService.listPlatforms();
        return ResponseHelper.buildSuccessfulResponse(platforms);
    }

    @Operation(summary = "获取详情", description = "获取存储平台详情")
    @GetMapping("/{id}")
    public GenericResponse<StoragePlatform> getStoragePlatform(@PathVariable("id") Long id) {
        StoragePlatform platform = storagePlatformService.getStoragePlatform(id);
        return ResponseHelper.buildSuccessfulResponse(platform);
    }

    @Operation(summary = "设置默认存储平台", description = "设置默认存储平台")
    @PutMapping("/{id}/default")
    public GenericResponse<StoragePlatform> setDefaultStoragePlatform(@PathVariable("id") Long id) {
        storagePlatformService.updateDefaultStorage(id);
        StoragePlatform platform = storagePlatformService.getDefaultStoragePlatform();
        return ResponseHelper.buildSuccessfulResponse(platform);
    }

    @Operation(summary = "创建存储平台", description = "创建存储平台")
    @PostMapping
    public GenericResponse<StoragePlatform> createStoragePlatform(@RequestBody StoragePayload payload) {
        StoragePlatform platform = storagePlatformService.createStoragePlatform(payload);
        return ResponseHelper.buildSuccessfulResponse(platform);
    }

    @Operation(summary = "更新存储平台", description = "更新存储平台")
    @PutMapping("/{id}")
    public GenericResponse<StoragePlatform> updateStoragePlatform(@PathVariable("id") Long id, @RequestBody StoragePayload payload) {
        payload.setId(id);
        StoragePlatform platform = storagePlatformService.updateStoragePlatform(payload);
        return ResponseHelper.buildSuccessfulResponse(platform);
    }

}
