package com.onezol.vertx.framework.security.api.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.onezol.vertx.framework.common.model.PagePack;
import com.onezol.vertx.framework.common.mvc.service.BaseService;
import com.onezol.vertx.framework.security.api.model.dto.User;
import com.onezol.vertx.framework.security.api.model.entity.RoleEntity;
import com.onezol.vertx.framework.security.api.model.entity.UserRoleEntity;

import java.util.List;

public interface UserRoleService extends BaseService<UserRoleEntity> {

    /**
     * 获取用户的角色
     *
     * @param userId 用户ID
     */
    List<RoleEntity> getUserRoles(Long userId);


    /**
     * 分配角色给用户
     *
     * @param roleId  角色ID
     * @param userIds 用户ID列表
     */
    void assignToUsers(Long roleId, List<Long> userIds);

    /**
     * 解绑用户所有角色
     *
     * @param userId 用户ID
     */
    void unbindUserAllRoles(Long userId);

    /**
     * 更新用户角色
     *
     * @param userId    用户ID
     * @param roleCodes 角色编码列表
     */
    void updateUserRoles(Long userId, List<String> roleCodes);

    /**
     * 获取角色下的用户列表
     *
     * @param page   分页对象
     * @param roleId 角色ID
     * @return 用户列表
     */
    PagePack<User> getRoleUsers(Page<UserRoleEntity> page, Long roleId);

    /**
     * 解绑用户角色
     *
     * @param roleId  角色ID
     * @param userIds 用户ID列表
     */
    void unbindUserRole(Long roleId, List<Long> userIds);

    /**
     * 角色绑定权限
     *
     * @param roleId        角色ID
     * @param permissionIds 权限ID列表
     */
    void bindRolePermissions(Long roleId, List<Long> permissionIds);

}
