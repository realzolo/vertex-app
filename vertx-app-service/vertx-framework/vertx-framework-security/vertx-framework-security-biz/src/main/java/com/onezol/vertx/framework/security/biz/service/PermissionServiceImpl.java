package com.onezol.vertx.framework.security.biz.service;

import com.onezol.vertx.framework.common.mvc.service.BaseServiceImpl;
import com.onezol.vertx.framework.common.util.StringUtils;
import com.onezol.vertx.framework.security.api.model.entity.PermissionEntity;
import com.onezol.vertx.framework.security.api.service.PermissionService;
import com.onezol.vertx.framework.security.api.service.RolePermissionService;
import com.onezol.vertx.framework.security.biz.mapper.PermissionMapper;
import lombok.extern.slf4j.Slf4j;
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

    public PermissionServiceImpl(RolePermissionService rolePermissionService) {
        this.rolePermissionService = rolePermissionService;
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
}
