package com.onezol.vertex.framework.support.mapper;

import com.onezol.vertex.framework.common.model.entity.ExceptionLogEntity;
import com.onezol.vertex.framework.common.mvc.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ExceptionLogMapper extends BaseMapper<ExceptionLogEntity> {
}
