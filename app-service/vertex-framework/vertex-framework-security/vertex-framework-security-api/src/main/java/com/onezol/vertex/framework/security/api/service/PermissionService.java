package com.onezol.vertex.framework.security.api.service;

import com.onezol.vertex.framework.common.mvc.service.BaseService;
import com.onezol.vertex.framework.security.api.model.entity.PermissionEntity;

public interface PermissionService extends BaseService<PermissionEntity> {

    /**
     * 获取用户权限
     *
     * @param roleId 角色ID
     */
    String getRolePermissions(Long roleId);

}
