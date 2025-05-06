package com.onezol.vertx.framework.security.biz.mapper;

import com.onezol.vertx.framework.common.skeleton.mapper.BaseMapper;
import com.onezol.vertx.framework.security.api.model.entity.RolePermissionEntity;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface RolePermissionMapper extends BaseMapper<RolePermissionEntity> {

    /**
     * 根据角色ID删除角色权限关联
     *
     * @param roleId 角色ID
     */
    void removePermissionsByRoleId(Long roleId);

    /**
     * 根据权限ID删除角色权限关联
     *
     * @param permissionId 权限ID
     */
    void removePermissionByPermissionId(Long permissionId);

}
