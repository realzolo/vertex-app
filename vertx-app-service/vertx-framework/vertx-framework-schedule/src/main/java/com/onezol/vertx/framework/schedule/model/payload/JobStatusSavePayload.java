package com.onezol.vertx.framework.schedule.model.payload;

import com.onezol.vertx.framework.common.model.payload.BasePayload;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Schema(description = "修改任务状态信息")
@Data
@EqualsAndHashCode(callSuper = true)
public class JobStatusSavePayload extends BasePayload {

    @Schema(description = "任务状态", example = "1")
    @NotNull(message = "任务状态无效")
    private Integer jobStatus;

}
