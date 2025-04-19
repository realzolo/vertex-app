package com.onezol.vertex.framework.security.biz.mapper;

import com.onezol.vertex.framework.common.mvc.mapper.BaseMapper;
import com.onezol.vertex.framework.security.api.model.entity.RolePermissionEntity;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface RolePermissionMapper extends BaseMapper<RolePermissionEntity> {

    /**
     * 根据角色ID删除角色权限关联
     * @param roleId 角色ID
     */
    void removePermissionsByRole(Long roleId);

}
