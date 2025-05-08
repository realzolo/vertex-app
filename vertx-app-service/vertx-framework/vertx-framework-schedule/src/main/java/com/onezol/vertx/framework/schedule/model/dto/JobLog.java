package com.onezol.vertx.framework.schedule.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.onezol.vertx.framework.common.skeleton.model.BaseDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

@Schema(description = "任务日志信息")
@Data
@EqualsAndHashCode(callSuper = true)
public class JobLog extends BaseDTO {

    @Schema(description = "任务组")
    private String groupName;

    @Schema(description = "任务名称")
    private String jobName;

    @Schema(description = "任务ID")
    private Long jobId;

    @Schema(description = "任务状态")
    private Integer taskBatchStatus;

    @Schema(description = "操作原因")
    private Integer operationReason;

    @Schema(description = "执行器类型")
    private Integer executorType;

    @Schema(description = "执行器名称")
    private String executorInfo;

    @Schema(description = "执行时间", type = "string")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime executionAt;

    @Schema(description = "创建时间", type = "string")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime createDt;

}
