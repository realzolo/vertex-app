package com.onezol.vertx.framework.schedule.model;

import com.aizuda.snailjob.common.core.model.Result;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Schema(description = "任务调度服务端分页返回对象")
@Data
@EqualsAndHashCode(callSuper = true)
public class JobPageResult<T> extends Result<T> {

    @Schema(description = "页码")
    private long page;

    @Schema(description = "每页条数")
    private long size;

    @Schema(description = "总条数")
    private long total;

}
