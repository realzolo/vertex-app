package com.onezol.vertx.framework.common.skeleton.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.onezol.vertx.framework.common.skeleton.model.BaseEntity;
import com.onezol.vertx.framework.common.skeleton.mapper.BaseMapper;
import com.onezol.vertx.framework.common.skeleton.model.DataEntity;

/**
 * 基础服务实现类
 */
public abstract class BaseServiceImpl<M extends BaseMapper<T>, T extends BaseEntity> extends ServiceImpl<M, T> implements BaseService<T> {
}
