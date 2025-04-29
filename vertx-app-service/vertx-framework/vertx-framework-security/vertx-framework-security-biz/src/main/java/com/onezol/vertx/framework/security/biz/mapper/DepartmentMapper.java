package com.onezol.vertx.framework.security.biz.mapper;

import com.onezol.vertx.framework.common.mvc.mapper.BaseMapper;
import com.onezol.vertx.framework.security.api.model.entity.DepartmentEntity;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface DepartmentMapper extends BaseMapper<DepartmentEntity> {
}
