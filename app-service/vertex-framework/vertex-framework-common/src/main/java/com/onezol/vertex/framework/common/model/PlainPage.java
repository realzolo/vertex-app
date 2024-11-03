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
public class PlainPage<T> {

    @Schema(description = "数据项", requiredMode = Schema.RequiredMode.REQUIRED)
    private Collection<T> items;

    @Schema(description = "总量", requiredMode = Schema.RequiredMode.REQUIRED)
    private long total;

    @Schema(description = "当前页码", requiredMode = Schema.RequiredMode.REQUIRED)
    private long pageNumber;

    @Schema(description = "当前页条数", requiredMode = Schema.RequiredMode.REQUIRED)
    private long pageSize;

    public PlainPage() {
        this.items = Collections.emptyList();
        this.total = 0;
        this.pageNumber = 1;
        this.pageSize = 0;
    }

    public PlainPage(Collection<T> items, long total) {
        this.items = items;
        this.total = total;
        this.pageNumber = 1;
        this.pageSize = items.size();
    }

    public PlainPage(Collection<T> items, long total, long pageNumber, long pageSize) {
        this.items = items;
        this.total = total;
        this.pageNumber = pageNumber;
        this.pageSize = pageSize;
    }

    public static <T> PlainPage<T> empty() {
        return new PlainPage<>(Collections.emptyList(), 0);
    }

    public static <T> PlainPage<T> of(Collection<T> items, Integer total) {
        return new PlainPage<>(items, total);
    }

    public static <T> PlainPage<T> of(Collection<T> items) {
        return new PlainPage<>(items, items.size());
    }

    public static <T> PlainPage<T> singleton(T item) {
        return new PlainPage<>(Collections.singletonList(item), 1);
    }

    public static <T> PlainPage<T> from(IPage<T> page) {
        return new PlainPage<>(page.getRecords(), page.getTotal(), page.getCurrent(), page.getSize());
    }

    public static <T> PlainPage<T> from(IPage<?> page, Class<T> targetClass) {
        List<?> records = page.getRecords();
        Collection<T> objects = BeanUtils.toList(records, targetClass);
        return new PlainPage<>(objects, page.getTotal(), page.getCurrent(), page.getSize());
    }
}
