package com.onezol.vertx.framework.schedule.model.input;

import com.onezol.vertx.framework.schedule.enums.JobStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import java.io.Serializable;

@Schema(description = "创建或修改任务参数")
@Data
public class JobSavePayload implements Serializable {

    @Schema(description = "任务ID", example = "1")
    private Long id;

    @Schema(description = "任务组")
    @NotBlank(message = "任务组不能为空")
    private String groupName;

    @Schema(description = "任务名称")
    @NotBlank(message = "任务名称不能为空")
    @Length(max = 64, message = "任务名称不能超过 {max} 个字符")
    private String jobName;

    @Schema(description = "描述")
    private String description;

    @Schema(description = "触发类型")
    @NotNull(message = "触发类型无效")
    private Integer triggerType;

    @Schema(description = "间隔时长")
    @NotBlank(message = "间隔时长不能为空")
    private String triggerInterval;

    @Schema(description = "执行器类型", defaultValue = "1")
    private Integer executorType = 1;

    @Schema(description = "任务类型")
    @NotNull(message = "任务类型无效")
    private Integer taskType;

    @Schema(description = "执行器名称")
    @NotBlank(message = "执行器名称不能为空")
    private String executorInfo;

    @Schema(description = "任务参数")
    private String argsStr;

    @Schema(description = "参数类型")
    private Integer argsType;

    @Schema(description = "路由策略")
    @NotNull(message = "路由策略无效")
    private Integer routeKey;

    @Schema(description = "阻塞策略")
    @NotNull(message = "阻塞策略无效")
    private Integer blockStrategy;

    @Schema(description = "超时时间（单位：秒）")
    @NotNull(message = "超时时间不能为空")
    private Integer executorTimeout;

    @Schema(description = "最大重试次数")
    @NotNull(message = "最大重试次数不能为空")
    private Integer maxRetryTimes;

    @Schema(description = "重试间隔（单位：秒）")
    @NotNull(message = "重试间隔不能为空")
    private Integer retryInterval;

    @Schema(description = "并行数")
    @NotNull(message = "并行数不能为空")
    private Integer parallelNum;

    @Schema(description = "任务状态", defaultValue = "0")
    private Integer jobStatus = JobStatus.DISABLED.getValue();

}
