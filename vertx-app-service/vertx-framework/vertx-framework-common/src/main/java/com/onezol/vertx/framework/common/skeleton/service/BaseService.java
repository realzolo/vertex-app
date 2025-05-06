package com.onezol.vertx.framework.common.skeleton.service;

import com.onezol.vertx.framework.common.skeleton.model.BaseEntity;

/**
 * 基础服务接口
 */
public interface BaseService<T extends BaseEntity> extends com.baomidou.mybatisplus.extension.service.IService<T> {
}
