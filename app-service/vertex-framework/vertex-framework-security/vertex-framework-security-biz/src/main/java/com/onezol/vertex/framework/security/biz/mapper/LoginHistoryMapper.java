package com.onezol.vertex.framework.security.biz.mapper;

import com.onezol.vertex.framework.common.mvc.mapper.BaseMapper;
import com.onezol.vertex.framework.security.api.model.entity.LoginHistoryEntity;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface LoginHistoryMapper extends BaseMapper<LoginHistoryEntity> {
}
