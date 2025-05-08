package com.onezol.vertx.framework.schedule.model.input;

import com.onezol.vertx.framework.common.skeleton.model.PageQuery;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Schema(description = "任务查询条件")
@Data
@EqualsAndHashCode(callSuper = true)
public class JobQuery extends PageQuery {

    @Schema(description = "任务组")
    private String groupName;

    @Schema(description = "任务名称")
    private String jobName;

    @Schema(description = "任务状态")
    private Integer jobStatus;

}
