package com.onezol.vertx.framework.extension.schedule.model.query;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.time.LocalDateTime;

@Schema(description = "任务日志查询条件")
@Data
@EqualsAndHashCode(callSuper = true)
public class JobLogQuery extends JobPageQuery {

    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "任务ID", example = "1")
    private Long jobId;

    @Schema(description = "任务组", example = "continew-admin")
    private String groupName;

    @Schema(description = "任务名称", example = "定时任务1")
    private String jobName;

    @Schema(description = "任务批次状态", example = "1")
    private Integer taskBatchStatus;

    @Schema(description = "创建时间", example = "2023-08-08 00:00:00,2023-08-08 23:59:59")
    @Size(max = 2, message = "创建时间必须是一个范围")
    private LocalDateTime[] datetimeRange;

}