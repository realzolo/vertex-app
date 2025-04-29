package com.onezol.vertex.framework.security.biz.controller;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.onezol.vertex.framework.common.constant.enumeration.DisEnableStatus;
import com.onezol.vertex.framework.common.model.*;
import com.onezol.vertex.framework.common.mvc.controller.BaseController;
import com.onezol.vertex.framework.common.util.BeanUtils;
import com.onezol.vertex.framework.common.util.TreeUtils;
import com.onezol.vertex.framework.security.api.annotation.RestrictAccess;
import com.onezol.vertex.framework.security.api.context.AuthenticationContext;
import com.onezol.vertex.framework.security.api.enumeration.PermissionTypeEnum;
import com.onezol.vertex.framework.security.api.model.UserIdentity;
import com.onezol.vertex.framework.security.api.model.dto.Permission;
import com.onezol.vertex.framework.security.api.model.dto.Role;
import com.onezol.vertex.framework.security.api.model.dto.User;
import com.onezol.vertex.framework.security.api.model.entity.PermissionEntity;
import com.onezol.vertex.framework.security.api.model.entity.RoleEntity;
import com.onezol.vertex.framework.security.api.model.entity.RolePermissionEntity;
import com.onezol.vertex.framework.security.api.model.entity.UserRoleEntity;
import com.onezol.vertex.framework.security.api.service.PermissionService;
import com.onezol.vertex.framework.security.api.service.RolePermissionService;
import com.onezol.vertex.framework.security.api.service.RoleService;
import com.onezol.vertex.framework.security.api.service.UserRoleService;
import com.onezol.vertex.framework.support.support.ResponseHelper;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;
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
    @Autowired
    private UserRoleService userRoleService;

    @GetMapping("/route")
    public GenericResponse<List<TreeNode>> route() {
        UserIdentity userIdentity = AuthenticationContext.get();
        List<DataPairRecord> roles = userIdentity.getRoles();
        Set<Long> roleIds = roles.stream().map(DataPairRecord::getId).collect(Collectors.toSet());
        Set<Long> permissionIds = rolePermissionService.list(
                Wrappers.<RolePermissionEntity>lambdaQuery()
                        .select(RolePermissionEntity::getPermissionId)
                        .in(!roleIds.isEmpty(), RolePermissionEntity::getRoleId, roleIds)
        ).stream().map(RolePermissionEntity::getPermissionId).collect(Collectors.toSet());
        List<PermissionEntity> permissions = permissionService.list(
                Wrappers.<PermissionEntity>lambdaQuery()
                        .eq(PermissionEntity::getStatus, DisEnableStatus.ENABLE)
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
    public GenericResponse<PagePack<RoleEntity>> getRolePage(
            @RequestParam(value = "page", required = false) Long pageNumber,
            @RequestParam(value = "size", required = false) Long pageSize
    ) {
        Page<RoleEntity> page = this.getPageObject(pageNumber, pageSize);
        Page<RoleEntity> quriedPage = roleService.page(page);
        PagePack<RoleEntity> pack = PagePack.from(quriedPage);
        return ResponseHelper.buildSuccessfulResponse(pack);
    }

    @GetMapping("/list")
    public GenericResponse<List<Role>> getRoles() {
        List<RoleEntity> list = roleService.list();
        List<Role> roles = BeanUtils.toList(list, Role.class);
        return ResponseHelper.buildSuccessfulResponse(roles);
    }

    @GetMapping("/dict")
    public GenericResponse<List<DictionaryEntry>> getRoleDict() {
        List<RoleEntity> roles = roleService.list();
        List<DictionaryEntry> options = new ArrayList<>(roles.size());
        for (RoleEntity role : roles) {
            options.add(DictionaryEntry.of(role.getName(), role.getCode()));
        }
        return ResponseHelper.buildSuccessfulResponse(options);
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

    @Operation(summary = "绑定角色用户")
    @PostMapping("/bind-users/{roleId}")
    public GenericResponse<Void> assignToUsers(@PathVariable("roleId") Long roleId, @RequestBody List<Long> userIds) {
        userRoleService.assignToUsers(roleId, userIds);
        return ResponseHelper.buildSuccessfulResponse();
    }

    @Operation(summary = "查询角色下的用户列表")
    @GetMapping("/users/{roleId}")
    public GenericResponse<PagePack<User>> getRoleUsers(
            @RequestParam("pageNumber") Long pageNumber,
            @RequestParam("pageSize") Long pageSize,
            @PathVariable("roleId") Long roleId
    ) {
        Page<UserRoleEntity> page = new Page<>(pageNumber, pageSize);
        PagePack<User> pack = userRoleService.getRoleUsers(page, roleId);
        return ResponseHelper.buildSuccessfulResponse(pack);
    }

    @Operation(summary = "删除用户-角色关系")
    @DeleteMapping("/unbind-users/{roleId}")
    public GenericResponse<Void> deleteUserRole(
            @PathVariable("roleId") Long roleId,
            @RequestBody List<Long> userIds
    ) {
        userRoleService.unbindUserRole(roleId, userIds);
        return ResponseHelper.buildSuccessfulResponse();
    }

    @Operation(summary = "角色绑定权限")
    @PutMapping("/bind-permissions/{roleId}")
    public GenericResponse<Void> bindPermissions(
            @PathVariable("roleId") Long roleId,
            @RequestBody Map<String, Object> body
    ) {
        List<Long> permissionIds = ((List<?>) body.get("permissionIds")).stream().map(id -> Long.parseLong(id.toString())).collect(Collectors.toList());
        userRoleService.bindRolePermissions(roleId, permissionIds);
        return ResponseHelper.buildSuccessfulResponse();
    }

}
