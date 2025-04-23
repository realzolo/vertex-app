package com.onezol.vertex.framework.component.approval.model.entity;

import com.onezol.vertex.framework.common.model.entity.BaseEntity;
import com.onezol.vertex.framework.component.approval.constant.ApprovalFlowStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class ApprovalFlowInstanceEntity extends BaseEntity {

    @Schema(name = "流程实例编号")
    private String instanceNo;

    @Schema(name = "关联的模板ID")
    private Long flowTemplateId;

    @Schema(name = "业务类型")
    private String businessTypeCode;

    @Schema(name = "业务ID")
    private String businessId;

    @Schema(name = "当前节点")
    private String currentNode;

    @Schema(name = "状态")
    private ApprovalFlowStatus status;

    @Schema(name = "发起人ID")
    private Long initiatorId;

}
