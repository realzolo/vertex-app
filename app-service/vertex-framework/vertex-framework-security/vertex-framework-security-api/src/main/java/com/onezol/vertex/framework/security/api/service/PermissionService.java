package com.onezol.vertex.framework.security.api.service;

import com.onezol.vertex.framework.common.mvc.service.BaseService;
import com.onezol.vertex.framework.security.api.model.dto.Permission;
import com.onezol.vertex.framework.security.api.model.entity.PermissionEntity;

import java.util.List;
import java.util.Set;

public interface PermissionService extends BaseService<PermissionEntity> {

    /**
     * 获取用户权限
     *
     * @param roleId 角色ID
     */
    String getRolePermissions(Long roleId);

    /**
     * 获取用户权限
     *
     * @param roleIds 角色ID
     */
//    List<Permission> getRolePermissions(List<Long> roleIds);

    /**
     * 获取用户权限
     *
     * @param roleIds 角色ID
     */
    Set<String> getRolePermissionKeys(List<Long> roleIds);

    /**
     * 获取用户权限
     *
     * @param roleIds 角色ID
     */
    Set<Long> getRolePermissionIds(List<Long> roleIds);

}
