package com.onezol.vertx.framework.common.skeleton.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.onezol.vertx.framework.common.skeleton.model.BaseEntity;

import java.util.Objects;

import static com.onezol.vertx.framework.common.constant.DefaultPage.*;


/**
 * 基础Controller
 *
 * @param <T>
 */
public abstract class BaseController<T extends BaseEntity> {

    /**
     * 获取分页对象
     *
     * @param pageNumber 当前页
     * @param pageSize   每页显示条数
     * @return 分页对象
     */
    @Deprecated
    protected Page<T> getPageObject(Long pageNumber, Long pageSize) {
        if (Objects.isNull(pageNumber)) {
            pageNumber = DEFAULT_PAGE_NUMBER;
        }
        if (Objects.isNull(pageSize)) {
            pageSize = DEFAULT_PAGE_SIZE;
        }
        pageSize = Math.min(pageSize, MAX_PAGE_SIZE);

        return new Page<>(pageNumber, pageSize);
    }

}
