package com.onezol.vertx.framework.common.mvc.mapper;

import com.onezol.vertx.framework.common.model.entity.Entity;
import org.apache.ibatis.annotations.Mapper;

/**
 * 基础Mapper
 *
 * @param <T> BaseEntity的子类
 */
@Mapper
@SuppressWarnings("ALL")
public interface BaseMapper<T extends Entity> extends com.baomidou.mybatisplus.core.mapper.BaseMapper<T> {
}
