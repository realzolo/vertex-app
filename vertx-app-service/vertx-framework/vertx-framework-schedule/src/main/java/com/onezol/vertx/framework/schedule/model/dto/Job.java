package com.onezol.vertx.framework.schedule.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.onezol.vertx.framework.common.skeleton.model.BaseDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

@Schema(description = "任务信息")
@Data
@EqualsAndHashCode(callSuper = true)
public class Job extends BaseDTO {

    @Schema(description = "任务组")
    private String groupName;

    @Schema(description = "任务名称")
    private String jobName;

    @Schema(description = "描述")
    private String description;

    @Schema(description = "触发类型")
    private Integer triggerType;

    @Schema(description = "间隔时长")
    private Integer triggerInterval;

    @Schema(description = " 执行器类型")
    private Integer executorType;

    @Schema(description = "执行器名称")
    private String executorInfo;

    @Schema(description = "任务类型")
    private Integer taskType;

    @Schema(description = "任务参数")
    private String argsStr;

    @Schema(description = "参数类型")
    private String argsType;

    @Schema(description = "路由策略")
    private Integer routeKey;

    @Schema(description = "阻塞策略")
    private Integer blockStrategy;

    @Schema(description = "超时时间（单位：秒）")
    private Integer executorTimeout;

    @Schema(description = "最大重试次数")
    private Integer maxRetryTimes;

    @Schema(description = "重试间隔")
    private Integer retryInterval;

    @Schema(description = "并行数")
    private Integer parallelNum;

    @Schema(description = "任务状态")
    private Integer jobStatus;

    @Schema(description = "下次触发时间", type = "string")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime nextTriggerAt;

    @Schema(description = "创建时间", type = "string")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime createDt;

    @Schema(description = "修改时间", type = "string")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime updateDt;

}