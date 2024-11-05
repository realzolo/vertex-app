package com.onezol.vertex.framework.component.configuration.mapper;

import com.onezol.vertex.framework.common.mvc.mapper.BaseMapper;
import com.onezol.vertex.framework.component.configuration.model.RuntimeConfigurationEntity;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface RuntimeConfigurationMapper extends BaseMapper<RuntimeConfigurationEntity> {
}
