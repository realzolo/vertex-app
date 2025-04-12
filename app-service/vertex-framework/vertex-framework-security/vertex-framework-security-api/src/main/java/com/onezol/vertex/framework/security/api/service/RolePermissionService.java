package com.onezol.vertex.framework.security.api.service;

import com.onezol.vertex.framework.common.mvc.service.BaseService;
import com.onezol.vertex.framework.security.api.model.entity.RolePermissionEntity;

public interface RolePermissionService extends BaseService<RolePermissionEntity> {

    /**
     * 根据角色ID删除角色权限关联
     * @param roleId 角色ID
     */
    void removePermissionsByRole(Long roleId);

}
