package com.onezol.vertx.framework.security.biz.controller;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.onezol.vertx.framework.common.model.GenericResponse;
import com.onezol.vertx.framework.common.model.TreeNode;
import com.onezol.vertx.framework.common.util.BeanUtils;
import com.onezol.vertx.framework.security.api.model.dto.Permission;
import com.onezol.vertx.framework.security.api.model.entity.PermissionEntity;
import com.onezol.vertx.framework.security.api.service.PermissionService;
import com.onezol.vertx.framework.support.support.ResponseHelper;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/permission")
public class PermissionController {

    private final PermissionService permissionService;

    public PermissionController(PermissionService permissionService) {
        this.permissionService = permissionService;
    }

    @GetMapping("/tree")
    @PreAuthorize("@Security.hasPermission('system:permission:list')")
    public GenericResponse<List<TreeNode>> getPermissionTree() {
        List<TreeNode> treeNodes = permissionService.tree();
        return ResponseHelper.buildSuccessfulResponse(treeNodes);
    }

    @GetMapping("/{id}")
    @PreAuthorize("@Security.hasPermission('system:permission:detail')")
    public GenericResponse<Permission> getPermissionTree(@PathVariable("id") Long id) {
        PermissionEntity entity = permissionService.getById(id);
        Permission permission = BeanUtils.toBean(entity, Permission.class);
        permission.setType(entity.getType().getValue());
        permission.setStatus(entity.getStatus().getValue());
        return ResponseHelper.buildSuccessfulResponse(permission);
    }

    @PostMapping("/add")
    @PreAuthorize("@Security.hasPermission('system:permission:create')")
    public GenericResponse<Permission> addPermission(@RequestBody Permission permission) {
        PermissionEntity entity = BeanUtils.toBean(permission, PermissionEntity.class);
        boolean ok = permissionService.save(entity);
        if (!ok) {
            return ResponseHelper.buildFailedResponse("新增菜单/权限失败");
        }
        return ResponseHelper.buildSuccessfulResponse(permission);
    }

    @PutMapping("/{id}")
    @PreAuthorize("@Security.hasPermission('system:permission:update')")
    public GenericResponse<Permission> updatePermission(@RequestBody Permission permission) {
        PermissionEntity entity = BeanUtils.toBean(permission, PermissionEntity.class);
        Long rootId = entity.getId();
        List<PermissionEntity> subPermissions = this.findSubPermissions(new ArrayList<>(), rootId);
        subPermissions.forEach(item -> item.setStatus(entity.getStatus()));
        subPermissions.add(entity);
        boolean ok = permissionService.updateBatchById(subPermissions);
        if (!ok) {
            return ResponseHelper.buildFailedResponse("修改权限失败");
        }
        return ResponseHelper.buildSuccessfulResponse(permission);
    }


    @DeleteMapping("/{id}")
    @PreAuthorize("@Security.hasPermission('system:permission:delete')")
    public GenericResponse<Void> deletePermission(@PathVariable("id") Long id) {
        boolean ok = permissionService.deletePermission(id);
        if (!ok) {
            return ResponseHelper.buildFailedResponse("删除权限失败");
        }
        return ResponseHelper.buildSuccessfulResponse();
    }


    private List<PermissionEntity> findSubPermissions(List<PermissionEntity> list, Long parentId) {
        List<PermissionEntity> sublist = permissionService.list(Wrappers.<PermissionEntity>lambdaQuery().eq(PermissionEntity::getParentId, parentId));
        for (PermissionEntity permission : sublist) {
            this.findSubPermissions(list, permission.getId());
        }
        list.addAll(sublist);
        return list;
    }

}
