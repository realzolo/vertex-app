package com.onezol.vertex.framework.security.biz.service;

import com.onezol.vertex.framework.common.mvc.service.BaseServiceImpl;
import com.onezol.vertex.framework.common.util.StringUtils;
import com.onezol.vertex.framework.security.api.mapper.PermissionMapper;
import com.onezol.vertex.framework.security.api.model.dto.Permission;
import com.onezol.vertex.framework.security.api.model.entity.PermissionEntity;
import com.onezol.vertex.framework.security.api.service.PermissionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Slf4j
@Service
public class PermissionServiceImpl extends BaseServiceImpl<PermissionMapper, PermissionEntity> implements PermissionService {

    private final PermissionMapper permissionMapper;

    public PermissionServiceImpl(PermissionMapper permissionMapper) {
        this.permissionMapper = permissionMapper;
    }

    /**
     * 获取用户权限
     *
     * @param roleId 角色ID
     */
    @Override
    public String getRolePermissions(Long roleId) {
        if (roleId == null) {
            return "";
        }
        return permissionMapper.queryRolePermission(roleId);
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
        List<PermissionEntity> permissions = permissionMapper.queryRolePermissions(roleIds);
        Set<String> permissionKeys = new HashSet<>();
        for (PermissionEntity permission : permissions) {
            if (!StringUtils.isBlank(permission.getPermission())) {
                permissionKeys.add(permission.getPermission());
            }
        }
        return permissionKeys;
    }

    /**
     * 获取用户权限
     *
     * @param roleIds 角色ID
     */
    @Override
    public Set<Long> getRolePermissionIds(List<Long> roleIds) {
        if (roleIds == null || roleIds.isEmpty()) {
            return Collections.emptySet();
        }
        List<PermissionEntity> permissions = permissionMapper.queryRolePermissions(roleIds);
        Set<Long> permissionIds = new HashSet<>();
        for (PermissionEntity permission : permissions) {
            permissionIds.add(permission.getId());
        }
        return permissionIds;
    }
}
