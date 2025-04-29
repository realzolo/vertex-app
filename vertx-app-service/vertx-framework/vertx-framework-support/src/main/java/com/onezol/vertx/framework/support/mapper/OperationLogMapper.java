package com.onezol.vertx.framework.support.mapper;

import com.onezol.vertx.framework.common.model.entity.OperationLogEntity;
import com.onezol.vertx.framework.common.mvc.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface OperationLogMapper extends BaseMapper<OperationLogEntity> {
}
