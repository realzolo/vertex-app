package com.onezol.vertex.framework.security.biz.controller;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.onezol.vertex.framework.common.bean.AuthenticationContext;
import com.onezol.vertex.framework.common.constant.enumeration.DisEnableStatusEnum;
import com.onezol.vertex.framework.common.helper.ResponseHelper;
import com.onezol.vertex.framework.common.model.*;
import com.onezol.vertex.framework.common.mvc.controller.BaseController;
import com.onezol.vertex.framework.common.util.BeanUtils;
import com.onezol.vertex.framework.common.util.JsonUtils;
import com.onezol.vertex.framework.common.util.TreeUtils;
import com.onezol.vertex.framework.security.api.annotation.RestrictAccess;
import com.onezol.vertex.framework.security.api.enumeration.PermissionTypeEnum;
import com.onezol.vertex.framework.security.api.model.dto.Permission;
import com.onezol.vertex.framework.security.api.model.dto.Role;
import com.onezol.vertex.framework.security.api.model.entity.PermissionEntity;
import com.onezol.vertex.framework.security.api.model.entity.RoleEntity;
import com.onezol.vertex.framework.security.api.model.entity.RolePermissionEntity;
import com.onezol.vertex.framework.security.api.service.PermissionService;
import com.onezol.vertex.framework.security.api.service.RolePermissionService;
import com.onezol.vertex.framework.security.api.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


@RestrictAccess
@RestController
@RequestMapping("/role")
public class RoleController extends BaseController<RoleEntity> {
    @Autowired
    private RoleService roleService;
    @Autowired
    private PermissionService permissionService;
    @Autowired
    private RolePermissionService rolePermissionService;

    @GetMapping("/route")
    public GenericResponse<List<TreeNode>> route() {
        AuthUser authUser = AuthenticationContext.get();
        List<LabelValue<String, String>> roles = authUser.getRoles();
        Set<String> roleIds = roles.stream().map(LabelValue::getValue).collect(Collectors.toSet());
        Set<Long> permissionIds = rolePermissionService.list(
                Wrappers.<RolePermissionEntity>lambdaQuery()
                        .select(RolePermissionEntity::getPermissionId)
                        .in(!roleIds.isEmpty(), RolePermissionEntity::getRoleId, roleIds)
        ).stream().map(RolePermissionEntity::getPermissionId).collect(Collectors.toSet());
        List<PermissionEntity> permissions = permissionService.list(
                Wrappers.<PermissionEntity>lambdaQuery()
                        .eq(PermissionEntity::getStatus, DisEnableStatusEnum.ENABLE)
                        .ne(PermissionEntity::getType, PermissionTypeEnum.BUTTON)
                        .in(!permissionIds.isEmpty(), PermissionEntity::getId, permissionIds)
        );
        List<Permission> list = BeanUtils.toList(permissions, Permission.class);
        List<TreeNode> nodes = new ArrayList<>(list.size());
        for (Permission permission : list) {
            TreeNode node = new TreeNode();
            node.setId(permission.getId());
            node.setParentId(permission.getParentId());
            node.setTitle(permission.getTitle());
            node.setIcon(permission.getIcon());
            node.setDisabled(false);
            node.setData(permission);
            nodes.add(node);
        }
        List<TreeNode> tree = TreeUtils.buildTree(nodes, 0L);
        return ResponseHelper.buildSuccessfulResponse(tree);
    }

    @GetMapping("/page")
    public GenericResponse<PlainPage<RoleEntity>> getRolePage(
            @RequestParam(value = "page", required = false) Integer pageNumber,
            @RequestParam(value = "size", required = false) Integer pageSize
    ) {
        Page<RoleEntity> page = this.getPage(pageNumber, pageSize);
        Page<RoleEntity> quriedPage = roleService.page(page);
        PlainPage<RoleEntity> resultPage = PlainPage.from(quriedPage);
        return ResponseHelper.buildSuccessfulResponse(resultPage);
    }

    @GetMapping("/dict")
    public GenericResponse<List<LabelValue<String, Object>>> getRoleDict() {
        List<RoleEntity> roles = roleService.list();
        List<LabelValue<String, Object>> dictionaries = new ArrayList<>(roles.size());
        for (RoleEntity role : roles) {
            dictionaries.add(new LabelValue<>(role.getName(), role.getCode()));
        }
        return ResponseHelper.buildSuccessfulResponse(dictionaries);
    }

    @GetMapping("/{id}")
    public GenericResponse<Role> getRoleInfo(@PathVariable(value = "id") Long roleId) {
        RoleEntity entity = roleService.getById(roleId);
        Role role = BeanUtils.toBean(entity, Role.class);
        Set<Long> permissionIds = permissionService.getRolePermissionIds(Collections.singletonList(entity.getId()));
        role.setPermissionIds(permissionIds);
        return ResponseHelper.buildSuccessfulResponse(role);
    }

    @PutMapping("/{id}")
    public GenericResponse<Void> updateRole(@RequestBody Role role) {
        roleService.updateRole(role);
        return ResponseHelper.buildSuccessfulResponse();
    }
}
