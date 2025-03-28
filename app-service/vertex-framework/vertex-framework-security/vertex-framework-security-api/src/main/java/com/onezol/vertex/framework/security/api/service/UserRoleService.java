package com.onezol.vertex.framework.security.api.service;

import com.onezol.vertex.framework.common.mvc.service.BaseService;
import com.onezol.vertex.framework.security.api.model.entity.RoleEntity;
import com.onezol.vertex.framework.security.api.model.entity.UserRoleEntity;

import java.util.List;
import java.util.Set;

public interface UserRoleService extends BaseService<UserRoleEntity> {

    /**
     * 获取用户的角色
     *
     * @param userId 用户ID
     */
    List<RoleEntity> getUserRoles(Long userId);

    /**
     * 解绑用户角色
     *
     * @param userId 用户ID
     */
    void unbindUserRole(Long userId);

    void updateUserRole(Long userId, List<Long> roleIds);

    void updateUserRoleByRoleCode(Long userId, List<String> roleCodes);

}
