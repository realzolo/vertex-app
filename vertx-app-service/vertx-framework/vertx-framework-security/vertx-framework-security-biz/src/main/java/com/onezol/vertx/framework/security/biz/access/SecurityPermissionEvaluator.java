package com.onezol.vertx.framework.security.biz.access;

import com.onezol.vertx.framework.security.api.context.AuthenticationContext;
import com.onezol.vertx.framework.security.api.model.UserIdentity;
import com.onezol.vertx.framework.security.api.service.PermissionService;
import com.onezol.vertx.framework.security.api.service.RoleService;
import org.springframework.stereotype.Component;

/**
 * 权限校验评估器
 */
@Component("Security")
public class SecurityPermissionEvaluator {

    private final RoleService roleService;
    private final PermissionService permissionService;

    public SecurityPermissionEvaluator(RoleService roleService, PermissionService permissionService) {
        this.roleService = roleService;
        this.permissionService = permissionService;
    }

    /**
     * 是否拥有指定权限
     *
     * @param permission 权限
     */
    public boolean hasPermission(String permission) {
        UserIdentity identity = AuthenticationContext.get();
        return permissionService.hasAnyPermissions(identity.getUserId(), permission);
    }

    /**
     * 是否拥有指定权限（任一一个即可）
     *
     * @param permissions 权限
     */
    public boolean hasAnyPermissions(String... permissions) {
        UserIdentity identity = AuthenticationContext.get();
        return permissionService.hasAnyPermissions(identity.getUserId(), permissions);
    }

    /**
     * 是否拥有指定角色
     *
     * @param role 角色编码, 如admin、Super
     */
    public boolean hasRole(String role) {
        UserIdentity identity = AuthenticationContext.get();
        return roleService.hasAnyRoles(identity.getUserId(), role);
    }

    /**
     * 是否拥有指定角色（任一一个即可）
     *
     * @param roles 角色数组
     */
    public boolean hasAnyRoles(String... roles) {
        UserIdentity identity = AuthenticationContext.get();
        return roleService.hasAnyRoles(identity.getUserId(), roles);
    }

}
