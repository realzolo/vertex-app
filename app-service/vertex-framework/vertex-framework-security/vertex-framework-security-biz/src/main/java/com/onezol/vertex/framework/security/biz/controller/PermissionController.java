package com.onezol.vertex.framework.security.biz.controller;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.onezol.vertex.framework.support.support.ResponseHelper;
import com.onezol.vertex.framework.common.model.GenericResponse;
import com.onezol.vertex.framework.common.model.TreeNode;
import com.onezol.vertex.framework.common.util.BeanUtils;
import com.onezol.vertex.framework.common.util.TreeUtils;
import com.onezol.vertex.framework.security.api.model.dto.Permission;
import com.onezol.vertex.framework.security.api.model.entity.PermissionEntity;
import com.onezol.vertex.framework.security.api.service.PermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/permission")
public class PermissionController {

    @Autowired
    private PermissionService permissionService;

    public static List<TreeNode> toTreeNodes(List<PermissionEntity> list) {
        List<TreeNode> treeNodes = new ArrayList<>();
        for (PermissionEntity entity : list) {
            TreeNode treeNode = new TreeNode();
            treeNode.setId(entity.getId());
            treeNode.setParentId(entity.getParentId());
            treeNode.setTitle(entity.getTitle());
            treeNode.setIcon(entity.getIcon());
            treeNode.setDisabled(false);
            Permission permission = BeanUtils.toBean(entity, Permission.class);
            permission.setType(entity.getType().getValue());
            permission.setStatus(entity.getStatus().getValue());
            treeNode.setData(permission);
            treeNodes.add(treeNode);
        }
        return treeNodes;
    }

    @GetMapping("/tree")
    public GenericResponse<List<TreeNode>> getPermissionTree() {
        List<PermissionEntity> list = permissionService.list();
        List<TreeNode> nodes = toTreeNodes(list);
        List<TreeNode> treeNodes = TreeUtils.buildTree(nodes, 0L);
        return ResponseHelper.buildSuccessfulResponse(treeNodes);
    }

    @GetMapping("/{id}")
    public GenericResponse<Permission> getPermissionTree(@PathVariable("id") Long id) {
        PermissionEntity entity = permissionService.getById(id);
        Permission permission = BeanUtils.toBean(entity, Permission.class);
        permission.setType(entity.getType().getValue());
        permission.setStatus(entity.getStatus().getValue());
        return ResponseHelper.buildSuccessfulResponse(permission);
    }

    @PostMapping("/add")
    public GenericResponse<Permission> addPermission(@RequestBody Permission permission) {
        PermissionEntity entity = BeanUtils.toBean(permission, PermissionEntity.class);
        boolean ok = permissionService.save(entity);
        if (!ok) {
            return ResponseHelper.buildFailedResponse("新增菜单/权限失败");
        }
        return ResponseHelper.buildSuccessfulResponse(permission);
    }

    @PutMapping("/{id}")
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
    public GenericResponse<Void> deletePermission(@PathVariable("id") Long id) {
        boolean ok = permissionService.removeById(id);
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
