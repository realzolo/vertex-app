package com.onezol.vertx.framework.schedule.model.dto;

import com.onezol.vertx.framework.common.model.dto.BaseDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Schema(description = "任务实例信息")
@Data
@EqualsAndHashCode(callSuper = true)
public class JobInstance extends BaseDTO {

    @Schema(description = "任务组", example = "vertx-app")
    private String groupName;

    @Schema(description = "任务ID", example = "1")
    private Long jobId;

    @Schema(description = "任务批次ID", example = "1")
    private Long taskBatchId;

    @Schema(description = "执行状态", example = "1")
    private Integer taskStatus;

    @Schema(description = "重试次数", example = "1")
    private Integer retryCount;

    @Schema(description = "执行结果", example = "")
    private String resultMessage;

    @Schema(description = "客户端信息", example = "1812406095098114048@192.168.138.48:1789")
    private String clientInfo;

}
