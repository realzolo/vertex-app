package com.onezol.vertx.framework.component.approval.model.payload;

import com.onezol.vertx.framework.common.skeleton.model.BaseDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@Schema(description = "审批流程节点候选人参数")
@Data
@EqualsAndHashCode(callSuper = true)
public class ApprovalFlowNodeCandidatePayload extends BaseDTO {

    @Schema(description = "节点ID")
    private Long nodeId;;

    @Schema(description = "候选人策略")
    private Integer candidateStrategy;

    @Schema(description = "候选人ID列表")
    private List<Long> userIds;

    @Schema(description = "候选人角色ID列表")
    private List<Long> roleIds;

    @Schema(description = "候选人选择类型")
    private Integer candidateSelectionType;

    @Schema(description = "候选人选择范围")
    private Integer candidateSelectionScope;

    @Schema(description = "审批类型")
    private Integer approvalType;

    @Schema(description = "无审批人审批策略")
    private Integer unmannedStrategy;

}