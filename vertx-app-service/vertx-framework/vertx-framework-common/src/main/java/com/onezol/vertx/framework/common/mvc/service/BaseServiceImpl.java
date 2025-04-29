package com.onezol.vertx.framework.common.mvc.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.onezol.vertx.framework.common.model.entity.BaseEntity;
import com.onezol.vertx.framework.common.mvc.mapper.BaseMapper;

/**
 * 基础服务实现类
 */
public abstract class BaseServiceImpl<M extends BaseMapper<T>, T extends BaseEntity> extends ServiceImpl<M, T> implements BaseService<T> {
}
