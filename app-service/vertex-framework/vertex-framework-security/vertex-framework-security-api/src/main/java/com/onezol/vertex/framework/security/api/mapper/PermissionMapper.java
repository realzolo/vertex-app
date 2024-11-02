package com.onezol.vertex.framework.security.api.mapper;

import com.onezol.vertex.framework.common.mvc.mapper.BaseMapper;
import com.onezol.vertex.framework.security.api.model.entity.PermissionEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface PermissionMapper extends BaseMapper<PermissionEntity> {

    String queryRolePermission(long roleId);

    List<PermissionEntity> queryRolePermissions(@Param("roleIds") List<Long> roleIds);

}
