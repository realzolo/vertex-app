package com.onezol.vertx.framework.component.approval.model.dto;

import com.onezol.vertx.framework.common.model.entity.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Schema(description = "审批流程节点")
@Data
@EqualsAndHashCode(callSuper = true)
public class ApprovalFlowNode extends BaseEntity {

    @Schema(description = "流程模板ID")
    private Long templateId;

    @Schema(description = "节点ID")
    private String nodeId;

    @Schema(description = "节点名称")
    private String label;

    @Schema(description = "节点类型")
    private String type;

    @Schema(description = "上一个节点ID")
    private String prevNodeId;

    @Schema(description = "下一个节点ID")
    private String nextNodeId;

}
