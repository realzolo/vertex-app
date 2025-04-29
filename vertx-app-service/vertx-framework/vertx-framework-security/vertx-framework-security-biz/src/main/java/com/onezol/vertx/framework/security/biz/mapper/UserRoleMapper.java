package com.onezol.vertx.framework.security.biz.mapper;

import com.onezol.vertx.framework.common.mvc.mapper.BaseMapper;
import com.onezol.vertx.framework.security.api.model.entity.RoleEntity;
import com.onezol.vertx.framework.security.api.model.entity.UserRoleEntity;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface UserRoleMapper extends BaseMapper<UserRoleEntity> {

    /**
     * 根据用户ID查询用户角色列表
     * @param userId 用户ID
     * @return 角色列表
     */
    List<RoleEntity> queryUserRoles(long userId);

}
