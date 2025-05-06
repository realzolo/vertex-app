package com.onezol.vertx.framework.common.skeleton.service;

import com.onezol.vertx.framework.common.skeleton.model.DataEntity;

/**
 * 基础服务接口
 */
public interface BaseService<T extends DataEntity> extends com.baomidou.mybatisplus.extension.service.IService<T> {
}
