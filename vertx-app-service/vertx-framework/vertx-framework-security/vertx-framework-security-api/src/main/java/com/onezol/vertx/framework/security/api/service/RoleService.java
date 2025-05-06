package com.onezol.vertx.framework.security.api.service;

import com.onezol.vertx.framework.common.model.TreeNode;
import com.onezol.vertx.framework.common.skeleton.service.BaseService;
import com.onezol.vertx.framework.security.api.model.dto.Role;
import com.onezol.vertx.framework.security.api.model.entity.RoleEntity;

import java.util.List;

/**
 * 角色服务接口
 */
public interface RoleService extends BaseService<RoleEntity> {

    /**
     * 获取角色树形结构。
     *
     * @return 角色树形结构
     */
    List<TreeNode> tree();

    /**
     * 更新角色信息。
     *
     * @param role 包含要更新的角色信息的对象。
     * @return 返回更新后的角色对象。
     */
    Role updateRole(Role role);

    /**
     * 根据用户 ID 获取该用户所拥有的角色列表。
     *
     * @param userId 用户的唯一标识符。
     * @return 返回一个包含用户角色的列表，如果用户没有角色则返回空列表。
     */
    List<Role> getUserRoles(Long userId);

    /**
     * 检查指定用户是否为超级管理。
     *
     * @param userId 用户的唯一标识符。
     * @return 如果用户是超级管理员则返回 true，否则返回 false。
     */
    boolean hasAnySuperAdmin(Long userId);

    /**
     * 检查指定用户是否拥有任意一个指定的角色。
     *
     * @param userId    用户的唯一标识符。
     * @param roleCodes 角色编码列表，用于指定要检查的角色。
     * @return 如果用户拥有任意一个指定的角色则返回 true，否则返回 false。
     */
    boolean hasAnyRoles(Long userId, String... roleCodes);

}
