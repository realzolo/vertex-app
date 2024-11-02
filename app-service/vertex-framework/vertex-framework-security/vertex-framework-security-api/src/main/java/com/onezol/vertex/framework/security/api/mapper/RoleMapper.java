package com.onezol.vertex.framework.security.api.mapper;

import com.onezol.vertex.framework.common.mvc.mapper.BaseMapper;
import com.onezol.vertex.framework.security.api.model.entity.RoleEntity;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface RoleMapper extends BaseMapper<RoleEntity> {

    List<RoleEntity> queryUserRoles(long userId);

    void removeUserRole(Long userId);
}
