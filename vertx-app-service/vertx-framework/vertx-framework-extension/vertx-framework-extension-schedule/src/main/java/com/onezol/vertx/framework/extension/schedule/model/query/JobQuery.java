package com.onezol.vertx.framework.extension.schedule.model.query;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Schema(description = "任务查询条件")
@EqualsAndHashCode(callSuper = true)
public class JobQuery extends JobPageQuery {

    @Schema(description = "任务组", example = "vertx-app")
    private String groupName;

    @Schema(description = "任务名称", example = "定时任务1")
    private String jobName;

    @Schema(description = "任务状态", example = "1")
    private Integer jobStatus;

}
