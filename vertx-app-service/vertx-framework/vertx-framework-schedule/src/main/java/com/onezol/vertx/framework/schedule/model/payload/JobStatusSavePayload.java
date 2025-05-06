package com.onezol.vertx.framework.schedule.model.payload;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Schema(description = "修改任务状态信息")
@Data
public class JobStatusSavePayload {

    @Schema(description = "任务ID", example = "1")
    @NotNull(message = "任务ID无效")
    private Long id;

    @Schema(description = "任务状态", example = "1")
    @NotNull(message = "任务状态无效")
    private Integer jobStatus;

}
