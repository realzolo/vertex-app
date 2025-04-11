package com.onezol.vertex.framework.common.model;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.onezol.vertex.framework.common.util.BeanUtils;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Schema(description = "分页对象")
@Data
public class PageModel<T> {

    @Schema(description = "数据项", requiredMode = Schema.RequiredMode.REQUIRED)
    private Collection<T> items;

    @Schema(description = "总量", requiredMode = Schema.RequiredMode.REQUIRED)
    private long total;

    @Schema(description = "当前页码", requiredMode = Schema.RequiredMode.REQUIRED)
    private long pageNumber;

    @Schema(description = "当前页条数", requiredMode = Schema.RequiredMode.REQUIRED)
    private long pageSize;

    public PageModel() {
        this.items = Collections.emptyList();
        this.total = 0;
        this.pageNumber = 1;
        this.pageSize = 0;
    }

    public PageModel(Collection<T> items, long total) {
        this.items = items;
        this.total = total;
        this.pageNumber = 1;
        this.pageSize = items.size();
    }

    public PageModel(Collection<T> items, long total, long pageNumber, long pageSize) {
        this.items = items;
        this.total = total;
        this.pageNumber = pageNumber;
        this.pageSize = pageSize;
    }

    public static <T> PageModel<T> empty() {
        return new PageModel<>(Collections.emptyList(), 0);
    }

    public static <T> PageModel<T> of(Collection<T> items, Integer total) {
        return new PageModel<>(items, total);
    }

    public static <T> PageModel<T> of(Collection<T> items) {
        return new PageModel<>(items, items.size());
    }

    public static <T> PageModel<T> singleton(T item) {
        return new PageModel<>(Collections.singletonList(item), 1);
    }

    public static <T> PageModel<T> from(IPage<T> page) {
        return new PageModel<>(page.getRecords(), page.getTotal(), page.getCurrent(), page.getSize());
    }

    public static <T> PageModel<T> from(IPage<?> page, Class<T> targetClass) {
        List<?> records = page.getRecords();
        Collection<T> objects = BeanUtils.toList(records, targetClass);
        return new PageModel<>(objects, page.getTotal(), page.getCurrent(), page.getSize());
    }
}
