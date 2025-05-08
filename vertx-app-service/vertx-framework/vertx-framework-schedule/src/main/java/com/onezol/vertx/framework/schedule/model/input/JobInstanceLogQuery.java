package com.onezol.vertx.framework.schedule.model.input;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

import java.io.Serializable;

@Schema(description = "任务实例日志查询条件")
@Data
public class JobInstanceLogQuery implements Serializable {

    @Schema(description = "任务ID")
    private Long jobId;

    @Schema(description = "任务批次ID")
    private Long taskBatchId;

    @Schema(description = "任务实例ID")
    private Long taskId;

    @Schema(description = "开始ID")
    private Integer startId;

    @Schema(description = "起始索引")
    @Min(value = 0, message = "起始索引最小值为 {value}")
    private Integer fromIndex = 0;

    @Schema(description = "每页条数")
    @Range(min = 1, max = 1000, message = "每页条数（取值范围 {min}-{max}）")
    private Integer size = 50;

}
