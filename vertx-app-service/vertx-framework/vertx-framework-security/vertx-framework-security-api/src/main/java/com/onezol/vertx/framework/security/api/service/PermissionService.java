package com.onezol.vertx.framework.security.api.service;

import com.onezol.vertx.framework.common.mvc.service.BaseService;
import com.onezol.vertx.framework.security.api.model.entity.PermissionEntity;

import java.util.List;
import java.util.Set;

public interface PermissionService extends BaseService<PermissionEntity> {

    /**
     * 获取用户权限
     *
     * @param roleIds 角色ID
     */
    Set<String> getRolePermissionKeys(List<Long> roleIds);

    /**
     * 获取权限ID集合
     *
     * @param roleIds 角色ID
     */
    Set<Long> getRolePermissionIds(List<Long> roleIds);

    /**
     * 删除权限
     */
    boolean deletePermission(Long id);

}
