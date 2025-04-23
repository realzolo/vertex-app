package com.onezol.vertex.framework.component.approval.model.payload;

import com.onezol.vertex.framework.common.model.entity.BaseEntity;
import com.onezol.vertex.framework.common.model.payload.BasePayload;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class ApprovalFlowBindingRelationPayload extends BasePayload {

    @Schema(name = "流程模板ID")
    private Long flowTemplateId;

    @Schema(name = "业务类型编码")
    private String businessTypeCode;

}
