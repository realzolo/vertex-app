package com.onezol.vertx.framework.common.skeleton.mapper;

import com.onezol.vertx.framework.common.skeleton.model.DataEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * 基础Mapper
 *
 * @param <T> BaseEntity的子类
 */
@Mapper
public interface BaseMapper<T extends DataEntity> extends com.baomidou.mybatisplus.core.mapper.BaseMapper<T> {
}
