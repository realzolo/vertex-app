package com.onezol.vertx.framework.schedule.model.payload;

import com.onezol.vertx.framework.common.model.payload.BasePayload;
import com.onezol.vertx.framework.common.model.payload.Payload;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.io.Serializable;

@Schema(description = "执行任务参数")
@Data
public class JobTriggerPayload implements Payload {

    @Schema(description = "ID", example = "1")
    @NotNull(message = "ID不能为空")
    private Long jobId;

    @Schema(description = "方法参数")
    private String tmpArgsStr;

}
