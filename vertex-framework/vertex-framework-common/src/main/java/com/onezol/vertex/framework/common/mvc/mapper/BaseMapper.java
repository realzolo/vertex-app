package com.onezol.vertex.framework.common.mvc.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.onezol.vertex.framework.common.model.entity.Entity;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * 基础Mapper
 *
 * @param <T> BaseEntity的子类
 */
@Mapper
@SuppressWarnings("ALL")
public interface BaseMapper<T extends Entity> extends com.baomidou.mybatisplus.core.mapper.BaseMapper<T> {
}
