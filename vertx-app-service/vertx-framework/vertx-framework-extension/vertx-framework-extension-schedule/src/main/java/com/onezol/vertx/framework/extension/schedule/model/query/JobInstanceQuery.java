package com.onezol.vertx.framework.extension.schedule.model.query;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Schema(description = "任务实例查询条件")
@Data
public class JobInstanceQuery implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 任务 ID
     */
    @Schema(description = "任务ID", example = "1")
    private Long jobId;

    /**
     * 任务批次 ID
     */
    @Schema(description = "任务批次ID", example = "1")
    private Long taskBatchId;
}
