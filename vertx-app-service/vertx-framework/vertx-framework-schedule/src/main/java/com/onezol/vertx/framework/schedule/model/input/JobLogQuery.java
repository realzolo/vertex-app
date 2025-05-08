package com.onezol.vertx.framework.schedule.model.input;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Schema(description = "任务日志查询条件")
@Data
@EqualsAndHashCode(callSuper = true)
public class JobLogQuery extends JobQuery {

    @Schema(description = "任务ID")
    private Long jobId;

    @Schema(description = "任务组")
    private String groupName;

    @Schema(description = "任务名称")
    private String jobName;

    @Schema(description = "任务批次状态")
    private Integer taskBatchStatus;

//    @Schema(description = "创建时间", example = "2023-08-08 00:00:00,2023-08-08 23:59:59")
//    @Size(max = 2, message = "创建时间必须是一个范围")
//    private LocalDateTime[] datetimeRange;

}