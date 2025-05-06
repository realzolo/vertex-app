package com.onezol.vertx.framework.security.biz.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.onezol.vertx.framework.common.skeleton.mapper.BaseMapper;
import com.onezol.vertx.framework.security.api.model.entity.UserEntitySoft;
import com.onezol.vertx.framework.security.api.model.payload.UserQueryPayload;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface UserMapper extends BaseMapper<UserEntitySoft> {

    Page<UserEntitySoft> queryUserPage(@Param("page") Page<UserEntitySoft> page, @Param("param") UserQueryPayload payload);

    Page<UserEntitySoft> queryUnboundRoleUserPage(@Param("page") Page<UserEntitySoft> page, @Param("param") UserQueryPayload payload);

}
