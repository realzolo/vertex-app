package com.onezol.vertx.framework.extension.schedule.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.onezol.vertx.framework.common.model.dto.BaseDTO;
import com.onezol.vertx.framework.extension.schedule.enums.*;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

@Schema(description = "任务信息")
@Data
@EqualsAndHashCode(callSuper = true)
public class Job extends BaseDTO {

    @Schema(description = "任务组", example = "continew-admin")
    private String groupName;

    @Schema(description = "任务名称", example = "定时任务1")
    private String jobName;

    @Schema(description = "描述", example = "定时任务1的描述")
    private String description;

    @Schema(description = "触发类型", example = "2")
    private Integer triggerType;

    @Schema(description = "间隔时长", example = "60")
    private Integer triggerInterval;

    @Schema(description = " 执行器类型", example = "1")
    private Integer executorType;

    @Schema(description = "执行器名称", example = "test")
    private String executorInfo;

    @Schema(description = "任务类型", example = "1")
    private Integer taskType;

    @Schema(description = "任务参数", example = "")
    private String argsStr;

    @Schema(description = "参数类型", example = "1")
    private String argsType;

    @Schema(description = "路由策略", example = "1")
    private Integer routeKey;

    @Schema(description = "阻塞策略", example = "1")
    private Integer blockStrategy;

    @Schema(description = "超时时间（单位：秒）", example = "60")
    private Integer executorTimeout;

    @Schema(description = "最大重试次数", example = "3")
    private Integer maxRetryTimes;

    @Schema(description = "重试间隔", example = "1")
    private Integer retryInterval;

    @Schema(description = "并行数", example = "1")
    private Integer parallelNum;

    @Schema(description = "任务状态", example = "1")
    private Integer jobStatus;

    @Schema(description = "下次触发时间", example = "2023-08-08 08:09:00", type = "string")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime nextTriggerAt;

    @Schema(description = "创建时间", example = "2023-08-08 08:08:00", type = "string")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime createDt;

    @Schema(description = "修改时间", example = "2023-08-08 08:08:00", type = "string")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime updateDt;

}