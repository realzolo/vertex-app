package com.onezol.vertx.framework.security.biz.mapper;

import com.onezol.vertx.framework.common.skeleton.mapper.BaseMapper;
import com.onezol.vertx.framework.security.api.model.entity.LoginHistoryEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface LoginHistoryMapper extends BaseMapper<LoginHistoryEntity> {

    /**
     * 获取用户登录信息
     * @param userIds 用户ID集合
     * @return 用户登录信息集合
     */
    List<LoginHistoryEntity> queryUserLoginDetails(@Param("userIds") List<Long> userIds);

}
