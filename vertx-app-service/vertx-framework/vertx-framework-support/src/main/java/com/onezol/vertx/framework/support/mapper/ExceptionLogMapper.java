package com.onezol.vertx.framework.support.mapper;

import com.onezol.vertx.framework.common.model.ExceptionLogEntity;
import com.onezol.vertx.framework.common.skeleton.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ExceptionLogMapper extends BaseMapper<ExceptionLogEntity> {
}
