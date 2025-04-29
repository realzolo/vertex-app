package com.onezol.vertx.framework.component.configuration.mapper;

import com.onezol.vertx.framework.common.mvc.mapper.BaseMapper;
import com.onezol.vertx.framework.component.configuration.model.RuntimeConfigurationEntity;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface RuntimeConfigurationMapper extends BaseMapper<RuntimeConfigurationEntity> {
}
