package com.onezol.vertex.framework.common.mvc.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.onezol.vertex.framework.common.model.entity.Entity;

import java.io.Serializable;

/**
 * 基础服务接口
 */
public interface BaseService<T extends Entity> extends com.baomidou.mybatisplus.extension.service.IService<T> {
}
