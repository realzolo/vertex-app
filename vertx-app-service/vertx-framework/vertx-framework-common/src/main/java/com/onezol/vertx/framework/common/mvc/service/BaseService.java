package com.onezol.vertx.framework.common.mvc.service;

import com.onezol.vertx.framework.common.model.entity.Entity;

/**
 * 基础服务接口
 */
public interface BaseService<T extends Entity> extends com.baomidou.mybatisplus.extension.service.IService<T> {
}
