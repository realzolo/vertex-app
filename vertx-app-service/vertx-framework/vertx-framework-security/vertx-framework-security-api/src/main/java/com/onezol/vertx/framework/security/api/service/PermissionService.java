package com.onezol.vertx.framework.security.api.service;

import com.onezol.vertx.framework.common.model.TreeNode;
import com.onezol.vertx.framework.common.mvc.service.BaseService;
import com.onezol.vertx.framework.security.api.model.dto.Permission;
import com.onezol.vertx.framework.security.api.model.entity.PermissionEntity;

import java.util.List;

/**
 * 权限服务接口
 */
public interface PermissionService extends BaseService<PermissionEntity> {

    /**
     * 获取权限树
     *
     * @return 返回权限树
     */
    List<TreeNode> tree();

    /**
     * 根据角色 ID 列表获取用户权限标识符集合。
     *
     * @param roleIds 角色 ID 列表
     * @return 返回一个包含权限标识符的集合
     */
    List<String> getRolePermissionKeys(List<Long> roleIds);

    /**
     * 根据角色 ID 列表获取权限 ID 集合。
     *
     * @param roleIds 角色 ID 列表
     * @return 返回一个包含权限 ID 的集合
     */
    List<Long> getRolePermissionIds(List<Long> roleIds);

    /**
     * 根据权限 ID 删除权限。
     *
     * @param id 要删除的权限的 ID。
     * @return 如果删除成功返回 true，否则返回 false。
     */
    boolean deletePermission(Long id);

    /**
     * 检查指定用户是否拥有任意一个指定的权限。
     *
     * @param userId         用户 ID
     * @param permissionKeys 权限标识符数组
     * @return 如果用户拥有任意一个指定的权限返回 true，否则返回 false。
     */
    boolean hasAnyPermissions(Long userId, String... permissionKeys);

    /**
     * 根据权限 ID 列表获取按钮权限列表。
     *
     * @param permissionIds 权限 ID 列表
     */
    List<Permission> listEnabledButtons(List<Long> permissionIds);

}
