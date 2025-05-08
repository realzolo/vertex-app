package com.onezol.vertx.framework.schedule.model.dto;

import com.onezol.vertx.framework.common.skeleton.model.BaseDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Schema(description = "任务实例信息")
@Data
@EqualsAndHashCode(callSuper = true)
public class JobInstance extends BaseDTO {

    @Schema(description = "任务组")
    private String groupName;

    @Schema(description = "任务ID")
    private Long jobId;

    @Schema(description = "任务批次ID")
    private Long taskBatchId;

    @Schema(description = "执行状态")
    private Integer taskStatus;

    @Schema(description = "重试次数")
    private Integer retryCount;

    @Schema(description = "执行结果")
    private String resultMessage;

    @Schema(description = "客户端信息")
    private String clientInfo;

}
