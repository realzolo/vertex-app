package com.onezol.vertx.framework.schedule.model.query;

import com.onezol.vertx.framework.common.skeleton.model.PageQuery;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Schema(description = "任务查询条件")
@Data
@EqualsAndHashCode(callSuper = true)
public class JobQuery extends PageQuery {

    @Schema(description = "任务组", example = "vertx-app")
    private String groupName;

    @Schema(description = "任务名称", example = "定时任务1")
    private String jobName;

    @Schema(description = "任务状态", example = "1")
    private Integer jobStatus;

}
