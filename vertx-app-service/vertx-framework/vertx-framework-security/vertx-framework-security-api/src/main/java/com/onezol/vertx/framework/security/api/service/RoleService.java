package com.onezol.vertx.framework.security.api.service;

import com.onezol.vertx.framework.common.mvc.service.BaseService;
import com.onezol.vertx.framework.security.api.model.dto.Role;
import com.onezol.vertx.framework.security.api.model.entity.RoleEntity;

import java.util.List;

public interface RoleService extends BaseService<RoleEntity> {

    /**
     * 更新角色
     *
     * @param role 角色
     */
    void updateRole(Role role);

    /**
     * 获取用户角色
     *
     * @param userId 用户ID
     */
    List<Role> getUserRoles(Long userId);

    /**
     * 判断用户是否是超级管理员/管理员
     *
     * @param userId 用户ID
     */
    boolean hasAnySuperAdmin(Long userId);

    /**
     * 判断用户是否拥有指定角色（任一一个即可）
     *
     * @param userId    用户ID
     * @param roleCodes 角色编码列表
     */
    boolean hasAnyRoles(Long userId, String... roleCodes);

}
