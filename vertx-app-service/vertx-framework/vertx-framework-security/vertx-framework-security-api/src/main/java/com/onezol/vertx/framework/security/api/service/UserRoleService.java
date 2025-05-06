package com.onezol.vertx.framework.security.api.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.onezol.vertx.framework.common.model.PagePack;
import com.onezol.vertx.framework.common.skeleton.service.BaseService;
import com.onezol.vertx.framework.security.api.model.dto.Role;
import com.onezol.vertx.framework.security.api.model.dto.User;
import com.onezol.vertx.framework.security.api.model.entity.UserRoleEntity;

import java.util.List;

/**
 * 用户角色服务接口
 */
public interface UserRoleService extends BaseService<UserRoleEntity> {

    /**
     * 根据用户 ID 获取该用户所拥有的角色列表。
     *
     * @param userId 用户的唯一标识符，用于指定要查询角色的用户。
     * @return 返回一个包含用户角色信息的列表，如果用户没有角色则返回空列表。
     */
    List<Role> getUserRoles(Long userId);

    /**
     * 将指定角色分配给多个用户。
     *
     * @param roleId  要分配的角色的唯一标识符。
     * @param userIds 要分配角色的用户 ID 列表。
     */
    void assignRoleToUsers(Long roleId, List<Long> userIds);

    /**
     * 解除指定用户与所有角色的分配关系。
     *
     * @param userId 用户的唯一标识符，用于指定要解除角色分配的用户。
     */
    void unassignAllRolesFromUser(Long userId);

    /**
     * 更新指定用户的角色列表。
     * 该方法会先解除用户与所有现有角色的分配关系，然后将新的角色分配给用户。
     *
     * @param userId    用户的唯一标识符，用于指定要更新角色的用户。
     * @param roleCodes 新的角色编码列表，用于指定要分配给用户的角色。
     */
    void assignRolesForUser(Long userId, List<String> roleCodes);

    /**
     * 根据分页信息和角色 ID 获取该角色下的用户列表。
     *
     * @param page   分页对象，用于指定查询的页码和每页记录数。
     * @param roleId 角色的唯一标识符，用于指定要查询用户的角色。
     * @return 返回一个包含用户信息的分页包装对象。
     */
    PagePack<User> getRoleUsers(Page<UserRoleEntity> page, Long roleId);

    /**
     * 解除指定用户与指定角色的分配关系。
     *
     * @param roleId  要解除分配的角色的唯一标识符。
     * @param userIds 要解除角色分配的用户 ID 列表。
     */
    void unassignRoleFromUsers(Long roleId, List<Long> userIds);

    /**
     * 为指定角色分配多个权限。
     *
     * @param roleId        要分配权限的角色的唯一标识符。
     * @param permissionIds 要分配给角色的权限 ID 列表。
     */
    void assignPermissionsToRole(Long roleId, List<Long> permissionIds);

    /**
     * 检查指定用户是否为超级管理员。
     *
     * @param userId 用户的唯一标识符，用于指定要检查的用户。
     * @return 如果用户是超级管理员则返回 true，否则返回 false。
     */
    boolean hasAnySuperAdmin(Long userId);

    /**
     * 检查指定用户是否拥有任意一个指定的角色。
     *
     * @param userId    用户的唯一标识符，用于指定要检查的用户。
     * @param roleCodes 角色编码数组，用于指定要检查的角色。
     * @return 如果用户拥有任意一个指定的角色则返回 true，否则返回 false。
     */
    boolean hasAnyRoles(Long userId, String[] roleCodes);

}
