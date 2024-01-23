package com.onezol.vertex.framework.common.mvc.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.util.Objects;

import static com.onezol.vertex.framework.common.constant.Constants.DEFAULT_PAGE_SIZE;
import static com.onezol.vertex.framework.common.constant.Constants.MAX_PAGE_SIZE;

/**
 * 基础Controller
 *
 * @param <T>
 */
public abstract class BaseController<T> {

    /**
     * 获取分页对象
     *
     * @param pageNo   当前页
     * @param pageSize 每页显示条数
     * @return 分页对象
     */
    protected IPage<T> getPage(Integer pageNo, Integer pageSize) {
        if (Objects.isNull(pageNo)) {
            pageNo = 1;
        }
        if (Objects.isNull(pageSize)) {
            pageSize = Integer.valueOf(DEFAULT_PAGE_SIZE).intValue();
        }
        pageSize = Math.min(pageSize, MAX_PAGE_SIZE);

        return new Page<>(pageNo, pageSize);
    }
}
