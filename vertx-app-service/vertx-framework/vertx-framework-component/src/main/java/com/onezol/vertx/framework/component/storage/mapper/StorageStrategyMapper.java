package com.onezol.vertx.framework.component.storage.mapper;

import com.onezol.vertx.framework.common.skeleton.mapper.BaseMapper;
import com.onezol.vertx.framework.component.storage.model.StorageStrategyEntity;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface StorageStrategyMapper extends BaseMapper<StorageStrategyEntity> {
}
