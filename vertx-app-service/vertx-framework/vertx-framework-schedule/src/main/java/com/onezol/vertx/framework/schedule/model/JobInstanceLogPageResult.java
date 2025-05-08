package com.onezol.vertx.framework.schedule.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Schema(description = "任务实例日志分页信息")
@Data
public class JobInstanceLogPageResult implements Serializable {

    @Schema(description = "ID")
    private Long id;

    @Schema(description = "日志详情")
    private List message;

    @Schema(description = "异常信息")
    private String throwable;

    @Schema(description = "是否结束")
    private boolean isFinished;

    @Schema(description = "起始索引")
    private Integer fromIndex;

    @Schema(description = "下一个开始ID")
    private Long nextStartId;

}
