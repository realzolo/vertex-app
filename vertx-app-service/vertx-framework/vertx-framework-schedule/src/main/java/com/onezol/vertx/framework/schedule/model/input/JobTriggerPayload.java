package com.onezol.vertx.framework.schedule.model.input;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serializable;

@Schema(description = "执行任务参数")
@Data
public class JobTriggerPayload implements Serializable {

    @Schema(description = "ID")
    @NotNull(message = "ID不能为空")
    private Long jobId;

    @Schema(description = "方法参数")
    private String tmpArgsStr;

}
