package com.onezol.vertx.framework.component.storage.mapper;

import com.onezol.vertx.framework.common.skeleton.mapper.BaseMapper;
import com.onezol.vertx.framework.component.storage.model.entity.StoragePlatformEntity;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface StorageMapper extends BaseMapper<StoragePlatformEntity> {
}
