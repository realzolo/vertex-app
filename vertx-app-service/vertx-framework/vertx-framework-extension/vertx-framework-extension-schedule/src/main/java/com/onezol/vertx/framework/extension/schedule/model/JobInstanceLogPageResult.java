package com.onezol.vertx.framework.extension.schedule.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Schema(description = "任务实例日志分页信息")
@Data
public class JobInstanceLogPageResult implements Serializable {

    @Schema(description = "ID", example = "1")
    private Long id;

    @Schema(description = "日志详情")
    private List message;

    @Schema(description = "异常信息")
    private String throwable;

    @Schema(description = "是否结束", example = "true")
    private boolean isFinished;

    @Schema(description = "起始索引", example = "0")
    private Integer fromIndex;

    @Schema(description = "下一个开始ID", example = "9")
    private Long nextStartId;

}
