package com.onezol.vertx.framework.component.approval.model.dto;

import com.onezol.vertx.framework.common.skeleton.model.BaseDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class ApprovalFlowBindingRelation extends BaseDTO {

    @Schema(name = "流程模板ID")
    private Long flowTemplateId;

    @Schema(name = "流程模板名称")
    private String flowTemplateName;

    @Schema(name = "业务类型编码")
    private String businessTypeCode;

    @Schema(name = "业务类型名称")
    private String businessTypeName;

}
