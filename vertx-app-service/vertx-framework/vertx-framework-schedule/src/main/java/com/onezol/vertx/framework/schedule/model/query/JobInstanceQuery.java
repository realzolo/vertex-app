package com.onezol.vertx.framework.schedule.model.query;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

@Schema(description = "任务实例查询条件")
@Data
public class JobInstanceQuery implements Serializable {

    @Schema(description = "任务ID", example = "1")
    private Long jobId;

    @Schema(description = "任务批次ID", example = "1")
    private Long taskBatchId;

}
