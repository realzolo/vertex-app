package com.onezol.vertx.framework.security.biz.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.onezol.vertx.framework.common.mvc.mapper.BaseMapper;
import com.onezol.vertx.framework.security.api.model.entity.UserEntity;
import com.onezol.vertx.framework.security.api.model.payload.UserQueryPayload;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface UserMapper extends BaseMapper<UserEntity> {

    Page<UserEntity> queryUserPage(@Param("page") Page<UserEntity> page, @Param("param") UserQueryPayload payload);

    Page<UserEntity> queryUnboundRoleUserPage(@Param("page") Page<UserEntity> page, @Param("param") UserQueryPayload payload);

}
