package com.onezol.vertx.framework.security.biz.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.onezol.vertx.framework.common.model.DictionaryEntry;
import com.onezol.vertx.framework.common.model.GenericResponse;
import com.onezol.vertx.framework.common.model.PagePack;
import com.onezol.vertx.framework.common.model.TreeNode;
import com.onezol.vertx.framework.common.skeleton.controller.BaseController;
import com.onezol.vertx.framework.common.util.BeanUtils;
import com.onezol.vertx.framework.security.api.model.dto.Role;
import com.onezol.vertx.framework.security.api.model.dto.User;
import com.onezol.vertx.framework.security.api.model.entity.RoleEntity;
import com.onezol.vertx.framework.security.api.model.entity.UserRoleEntity;
import com.onezol.vertx.framework.security.api.service.PermissionService;
import com.onezol.vertx.framework.security.api.service.RoleService;
import com.onezol.vertx.framework.security.api.service.UserRoleService;
import com.onezol.vertx.framework.support.support.ResponseHelper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Tag(name = "角色管理")
@RestController
@RequestMapping("/role")
public class RoleController extends BaseController<RoleEntity> {

    private final RoleService roleService;
    private final PermissionService permissionService;
    private final UserRoleService userRoleService;

    public RoleController(RoleService roleService, PermissionService permissionService, UserRoleService userRoleService) {
        this.roleService = roleService;
        this.permissionService = permissionService;
        this.userRoleService = userRoleService;
    }

    @Operation(summary = "获取角色树")
    @GetMapping("/tree")
    @PreAuthorize("@Security.hasPermission('system:role:list')")
    public GenericResponse<List<TreeNode>> route() {
        List<TreeNode> tree = roleService.tree();
        return ResponseHelper.buildSuccessfulResponse(tree);
    }

    @Operation(summary = "分页查询角色列表")
    @GetMapping("/page")
    @PreAuthorize("@Security.hasPermission('system:role:list')")
    public GenericResponse<PagePack<RoleEntity>> getRolePage(
            @RequestParam(value = "page", required = false) Long pageNumber,
            @RequestParam(value = "size", required = false) Long pageSize
    ) {
        Page<RoleEntity> page = this.getPageObject(pageNumber, pageSize);
        Page<RoleEntity> quriedPage = roleService.page(page);
        PagePack<RoleEntity> pack = PagePack.from(quriedPage);
        return ResponseHelper.buildSuccessfulResponse(pack);
    }

    @Operation(summary = "获取角色列表")
    @GetMapping("/list")
    @PreAuthorize("@Security.hasPermission('system:role:list')")
    public GenericResponse<List<Role>> getRoles() {
        List<RoleEntity> list = roleService.list();
        List<Role> roles = BeanUtils.toList(list, Role.class);
        return ResponseHelper.buildSuccessfulResponse(roles);
    }

    @Operation(summary = "获取角色字典")
    @GetMapping("/dict")
    @PreAuthorize("@Security.hasPermission('system:role:list')")
    public GenericResponse<List<DictionaryEntry>> getRoleDict() {
        List<RoleEntity> roles = roleService.list();
        List<DictionaryEntry> options = new ArrayList<>(roles.size());
        for (RoleEntity role : roles) {
            options.add(DictionaryEntry.of(role.getName(), role.getCode()));
        }
        return ResponseHelper.buildSuccessfulResponse(options);
    }

    @Operation(summary = "获取角色信息")
    @GetMapping("/{id}")
    @PreAuthorize("@Security.hasPermission('system:role:detail')")
    public GenericResponse<Role> getRoleInfo(@PathVariable(value = "id") Long roleId) {
        RoleEntity entity = roleService.getById(roleId);
        Role role = BeanUtils.toBean(entity, Role.class);
        List<Long> permissionIds = permissionService.getRolePermissionIds(Collections.singletonList(entity.getId()));
        role.setPermissionIds(permissionIds);
        return ResponseHelper.buildSuccessfulResponse(role);
    }

    @Operation(summary = "更新角色信息")
    @PutMapping("/{id}")
    @PreAuthorize("@Security.hasPermission('system:role:update')")
    public GenericResponse<Role> updateRole(@RequestBody Role role) {
        role = roleService.updateRole(role);
        return ResponseHelper.buildSuccessfulResponse(role);
    }

    @Operation(summary = "将指定角色分配给多个用户")
    @PostMapping("/assign-role/{roleId}")
    @PreAuthorize("@Security.hasPermission('system:role:assign')")
    public GenericResponse<Void> assignRoleToUsers(@PathVariable("roleId") Long roleId, @RequestBody List<Long> userIds) {
        userRoleService.assignRoleToUsers(roleId, userIds);
        return ResponseHelper.buildSuccessfulResponse();
    }

    @Operation(summary = "查询角色下的用户列表")
    @GetMapping("/users/{roleId}")
    @PreAuthorize("@Security.hasPermission('system:role:list')")
    public GenericResponse<PagePack<User>> getRoleUsers(
            @RequestParam("pageNumber") Long pageNumber,
            @RequestParam("pageSize") Long pageSize,
            @PathVariable("roleId") Long roleId
    ) {
        Page<UserRoleEntity> page = new Page<>(pageNumber, pageSize);
        PagePack<User> pack = userRoleService.getRoleUsers(page, roleId);
        return ResponseHelper.buildSuccessfulResponse(pack);
    }

    @Operation(summary = "解除指定用户与指定角色的分配关系")
    @DeleteMapping("/unassign-role/{roleId}")
    @PreAuthorize("@Security.hasPermission('system:role:delete')")
    public GenericResponse<Void> unassignRoleFromUsers(
            @PathVariable("roleId") Long roleId,
            @RequestBody List<Long> userIds
    ) {
        userRoleService.unassignRoleFromUsers(roleId, userIds);
        return ResponseHelper.buildSuccessfulResponse();
    }

    // TODO: 移动到 permissionController
    @Operation(summary = "角色绑定权限")
    @PutMapping("/assign-permissions/{roleId}")
    @PreAuthorize("@Security.hasPermission('system:role:update-permission')")
    public GenericResponse<Void> assignPermissionsToRole(
            @PathVariable("roleId") Long roleId,
            @RequestBody Map<String, Object> body
    ) {
        List<Long> permissionIds = ((List<?>) body.get("permissionIds")).stream().map(id -> Long.parseLong(id.toString())).collect(Collectors.toList());
        userRoleService.assignPermissionsToRole(roleId, permissionIds);
        return ResponseHelper.buildSuccessfulResponse();
    }

}
