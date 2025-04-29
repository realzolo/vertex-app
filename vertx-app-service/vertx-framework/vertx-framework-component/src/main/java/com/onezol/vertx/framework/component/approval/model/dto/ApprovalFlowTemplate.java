package com.onezol.vertx.framework.component.approval.model.dto;

import com.onezol.vertx.framework.common.model.dto.BaseDTO;
import com.onezol.vertx.framework.component.approval.constant.ApprovalFlowType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class ApprovalFlowTemplate extends BaseDTO {

    @Schema(name = "流程名称")
    private String name;

    @Schema(name = "流程类型")
    private ApprovalFlowType type;

    @Schema(name = "流程内容")
    private String content;

    @Schema(name = "流程状态")
    private Integer status;

    @Schema(name = "流程备注")
    private String remark;

}
