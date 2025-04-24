package com.onezol.vertex.framework.common.model;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.onezol.vertex.framework.common.util.BeanUtils;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Collections;
import java.util.List;

@Schema(description = "分页对象")
@Data
public class PagePack<T> {

    @Schema(description = "数据项", requiredMode = Schema.RequiredMode.REQUIRED)
    private List<T> items;

    @Schema(description = "总量", requiredMode = Schema.RequiredMode.REQUIRED)
    private long total;

    @Schema(description = "当前页码", requiredMode = Schema.RequiredMode.REQUIRED)
    private long pageNumber;

    @Schema(description = "当前页条数", requiredMode = Schema.RequiredMode.REQUIRED)
    private long pageSize;

    public PagePack() {
        this.items = Collections.emptyList();
        this.total = 0;
        this.pageNumber = 1;
        this.pageSize = 0;
    }

    public PagePack(List<T> records, long total) {
        this.items = records;
        this.total = total;
        this.pageNumber = 1;
        this.pageSize = records.size();
    }

    public PagePack(List<T> records, long total, long pageNumber, long pageSize) {
        this.items = records;
        this.total = total;
        this.pageNumber = pageNumber;
        this.pageSize = pageSize;
    }

    public static <T> PagePack<T> empty() {
        return new PagePack<>(Collections.emptyList(), 0);
    }

    public static <T> PagePack<T> of(List<T> records, Integer total) {
        return new PagePack<>(records, total);
    }

    public static <T> PagePack<T> of(List<T> records) {
        return new PagePack<>(records, records.size());
    }

    public static <T> PagePack<T> of(List<T> records, long total, long pageNumber, long pageSize) {
        return new PagePack<>(records, total, pageNumber, pageSize);
    }

    public static <T> PagePack<T> singleton(T item) {
        return new PagePack<>(Collections.singletonList(item), 1);
    }

    public static <T> PagePack<T> from(IPage<T> page) {
        return new PagePack<>(page.getRecords(), page.getTotal(), page.getCurrent(), page.getSize());
    }

    public static <T> PagePack<T> from(IPage<?> page, Class<T> targetClass) {
        List<?> records = page.getRecords();
        List<T> objects = BeanUtils.toList(records, targetClass);
        return new PagePack<>(objects, page.getTotal(), page.getCurrent(), page.getSize());
    }
}
