package com.onezol.vertex.framework.security.biz.controller;

import com.onezol.vertex.framework.common.constant.enums.DisEnableStatusEnum;
import com.onezol.vertex.framework.common.constant.enums.Enum;
import com.onezol.vertex.framework.common.helper.ResponseHelper;
import com.onezol.vertex.framework.common.model.GenericResponse;
import com.onezol.vertex.framework.common.model.TreeNode;
import com.onezol.vertex.framework.common.util.BeanUtils;
import com.onezol.vertex.framework.security.api.enumeration.MenuTypeEnum;
import com.onezol.vertex.framework.security.api.model.dto.Permission;
import com.onezol.vertex.framework.security.api.model.entity.PermissionEntity;
import com.onezol.vertex.framework.security.api.service.PermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
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
            treeNode.setKey(entity.getId());
            treeNode.setParentKey(entity.getParentId());
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

    public static List<TreeNode> buildTree(List<TreeNode> nodes, Long parentId) {
        List<TreeNode> treeNodes = new ArrayList<>();
        for (TreeNode node : nodes) {
            if (node.getParentKey().toString().equals(parentId.toString())) {
                treeNodes.add(node);
            }
        }
        for (TreeNode node : treeNodes) {
            node.setChildren(buildTree(nodes, node.getKey()));
        }
        return treeNodes;
    }

    @GetMapping("/tree")
    public GenericResponse<List<TreeNode>> getPermissionTree() {
        List<PermissionEntity> list = permissionService.list();
        List<TreeNode> nodes = toTreeNodes(list);
        List<TreeNode> treeNodes = buildTree(nodes, 0L);
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
        switch (permission.getStatus()) {
            case 0 -> entity.setStatus(DisEnableStatusEnum.ENABLE);
            case 1 -> entity.setStatus(DisEnableStatusEnum.DISABLE);
        }
        boolean ok = permissionService.updateById(entity);
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
}
