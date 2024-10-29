package com.onezol.vertex.framework.common.mvc.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.util.Objects;

import static com.onezol.vertex.framework.common.constant.DefaultPage.DEFAULT_PAGE_SIZE;
import static com.onezol.vertex.framework.common.constant.DefaultPage.MAX_PAGE_SIZE;


/**
 * 基础Controller
 *
 * @param <T>
 */
public abstract class BaseController<T> {

    /**
     * 获取分页对象
     *
     * @param pageNumber   当前页
     * @param pageSize 每页显示条数
     * @return 分页对象
     */
    protected IPage<T> getPage(Integer pageNumber, Integer pageSize) {
        if (Objects.isNull(pageNumber)) {
            pageNumber = 1;
        }
        if (Objects.isNull(pageSize)) {
            pageSize = DEFAULT_PAGE_SIZE;
        }
        pageSize = Math.min(pageSize, MAX_PAGE_SIZE);

        return new Page<>(pageNumber, pageSize);
    }
}
