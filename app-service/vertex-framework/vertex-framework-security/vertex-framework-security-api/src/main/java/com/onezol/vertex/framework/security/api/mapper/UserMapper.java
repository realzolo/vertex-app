package com.onezol.vertex.framework.security.api.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.onezol.vertex.framework.common.mvc.mapper.BaseMapper;
import com.onezol.vertex.framework.security.api.model.entity.UserEntity;
import com.onezol.vertex.framework.security.api.model.payload.UserQueryPayload;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface UserMapper extends BaseMapper<UserEntity> {

    Page<UserEntity> queryUserPage(@Param("page") Page<UserEntity> page, @Param("param") UserQueryPayload payload);
}
