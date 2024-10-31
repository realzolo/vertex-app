package com.onezol.vertex.framework.security.api.service;

import com.onezol.vertex.framework.common.mvc.service.BaseService;
import com.onezol.vertex.framework.security.api.model.entity.RoleEntity;
import com.onezol.vertex.framework.security.api.model.entity.UserRoleEntity;

public interface UserRoleService extends BaseService<UserRoleEntity> {

    /**
     * 获取用户的角色
     *
     * @param userId 用户ID
     */
    RoleEntity getUserRole(Long userId);

    /**
     * 解绑用户角色
     *
     * @param userId 用户ID
     */
    void unbindUserRole(Long userId);
}
