package com.onezol.vertex.framework.common.mvc.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.onezol.vertex.framework.common.model.entity.Entity;

import java.util.Objects;

import static com.onezol.vertex.framework.common.constant.DefaultPage.*;


/**
 * 基础Controller
 *
 * @param <T>
 */
public abstract class BaseController<T extends Entity> {

    /**
     * 获取分页对象
     *
     * @param pageNumber 当前页
     * @param pageSize   每页显示条数
     * @return 分页对象
     */
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
