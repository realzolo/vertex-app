package com.onezol.vertx.framework.security.biz.service;

import com.onezol.vertx.framework.common.mvc.service.BaseServiceImpl;
import com.onezol.vertx.framework.common.util.StringUtils;
import com.onezol.vertx.framework.security.api.model.dto.Role;
import com.onezol.vertx.framework.security.api.model.entity.PermissionEntity;
import com.onezol.vertx.framework.security.api.service.PermissionService;
import com.onezol.vertx.framework.security.api.service.RolePermissionService;
import com.onezol.vertx.framework.security.api.service.RoleService;
import com.onezol.vertx.framework.security.biz.mapper.PermissionMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Slf4j
@Service
public class PermissionServiceImpl extends BaseServiceImpl<PermissionMapper, PermissionEntity> implements PermissionService {

    private final RolePermissionService rolePermissionService;
    private final RoleService roleService;

    public PermissionServiceImpl(RolePermissionService rolePermissionService, @Lazy RoleService roleService) {
        this.rolePermissionService = rolePermissionService;
        this.roleService = roleService;
    }

    /**
     * 获取用户权限
     *
     * @param roleIds 角色ID
     */
    @Override
    public Set<String> getRolePermissionKeys(List<Long> roleIds) {
        if (roleIds == null || roleIds.isEmpty()) {
            return Collections.emptySet();
        }
        List<PermissionEntity> permissions = this.baseMapper.queryRolePermissions(roleIds);
        Set<String> permissionKeys = new HashSet<>();
        for (PermissionEntity permission : permissions) {
            if (!StringUtils.isBlank(permission.getPermission())) {
                permissionKeys.add(permission.getPermission());
            }
        }
        return permissionKeys;
    }

    /**
     * 获取权限ID集合
     *
     * @param roleIds 角色ID
     */
    @Override
    public Set<Long> getRolePermissionIds(List<Long> roleIds) {
        if (roleIds == null || roleIds.isEmpty()) {
            return Collections.emptySet();
        }
        List<PermissionEntity> permissions = this.baseMapper.queryRolePermissions(roleIds);
        Set<Long> permissionIds = new HashSet<>();
        for (PermissionEntity permission : permissions) {
            permissionIds.add(permission.getId());
        }
        return permissionIds;
    }

    /**
     * 删除权限
     *
     * @param id 权限ID
     */
    @Override
    @Transactional
    public boolean deletePermission(Long id) {
        this.removeById(id);
        rolePermissionService.removePermissionByPermissionId(id);
        return true;
    }

    /**
     * 检查权限是否存在
     *
     * @param userId      用户ID
     * @param permissions 权限标识符
     */
    @Override
    public boolean hasAnyPermissions(Long userId, String[] permissions) {
        // 如果为空，说明已经有权限
        if (permissions == null || permissions.length == 0) {
            return true;
        }

        // 获得当前登录的角色。如果为空，说明没有权限
        List<Role> roles = roleService.getUserRoles(userId);
        if (CollectionUtils.isEmpty(roles)) {
            return false;
        }

        // 情况一：遍历判断每个权限，如果有一满足，说明有权限
        for (String permission : permissions) {
            if (this.hasAnyPermission(roles, permission)) {
                return true;
            }
        }

        // 情况二：如果是超管，也说明有权限
        return roleService.hasAnySuperAdmin(userId);
    }

    /**
     * 判断指定角色，是否拥有该 permission 权限
     *
     * @param roles      指定角色数组
     * @param permission 权限标识
     * @return 是否拥有
     */
    private boolean hasAnyPermission(List<Role> roles, String permission) {
        List<Long> roleIds = roles.stream().map(Role::getId).toList();
        Set<String> permissionKeys = this.getRolePermissionKeys(roleIds);
        return permissionKeys.stream().anyMatch(permission::equals);
    }

}
