package com.onezol.vertx.framework.schedule.model.input;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

@Schema(description = "任务实例查询条件")
@Data
public class JobInstanceQuery implements Serializable {

    @Schema(description = "任务ID")
    private Long jobId;

    @Schema(description = "任务批次ID")
    private Long taskBatchId;

}
