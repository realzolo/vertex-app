package com.onezol.vertx.framework.security.api.service;

import com.onezol.vertx.framework.common.skeleton.service.BaseService;
import com.onezol.vertx.framework.security.api.model.entity.RolePermissionEntity;

import java.util.List;
import java.util.Map;

/**
 * 角色权限服务接口
 */
public interface RolePermissionService extends BaseService<RolePermissionEntity> {

    /**
     * 根据权限 ID 删除角色与该权限的关联记录。
     *
     * @param permissionId 要删除关联的权限的唯一标识符。
     */
    void removePermissionByPermissionId(Long permissionId);

    /**
     * 根据角色 ID 列表获取每个角色对应的权限 ID 列表。
     *
     * @param roleIds 用于查询权限的角色 ID 列表。
     * @return 一个映射，包含每个角色 ID 及其对应的权限 ID 列表。
     */
    Map<Long, List<Long>> getPermissionIdsByRoleIds(List<Long> roleIds);

}
