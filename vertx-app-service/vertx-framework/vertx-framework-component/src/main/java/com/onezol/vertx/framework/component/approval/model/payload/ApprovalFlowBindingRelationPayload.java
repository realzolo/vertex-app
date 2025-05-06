package com.onezol.vertx.framework.component.approval.model.payload;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

@Data
public class ApprovalFlowBindingRelationPayload implements Serializable {

    @Schema(name = "流程模板ID")
    private Long flowTemplateId;

    @Schema(name = "业务类型编码")
    private String businessTypeCode;

}
