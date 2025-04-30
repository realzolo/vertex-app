package com.onezol.vertx.framework.component.approval.model.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import com.onezol.vertx.framework.common.model.entity.BaseEntity;
import com.onezol.vertx.framework.component.approval.constant.*;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@Schema(description = "审批流程节点候选人")
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("app_approval_flow_node_candidate")
public class ApprovalFlowNodeCandidateEntity extends BaseEntity {

    @Schema(description = "节点ID")
    @TableField("node_id")
    private Long nodeId;

    @Schema(description = "候选人策略")
    @TableField(value = "candidate_strategy")
    private CandidateStrategy candidateStrategy;

    @Schema(description = "候选人ID列表")
    @TableField(value = "user_ids", typeHandler = JacksonTypeHandler.class)
    private List<Long> userIds;

    @Schema(description = "候选人角色ID列表")
    @TableField(value = "role_ids", typeHandler = JacksonTypeHandler.class)
    private List<Long> roleIds;

    @Schema(description = "候选人选择类型")
    @TableField(value = "candidate_selection_type")
    private CandidateSelectionType candidateSelectionType;

    @Schema(description = "候选人选择范围")
    @TableField(value = "candidate_selection_scope")
    private CandidateSelectionScope candidateSelectionScope;

    @Schema(description = "审批类型")
    @TableField(value = "approval_type")
    private ApprovalType approvalType;

    @Schema(description = "无审批人审批策略")
    @TableField(value = "unmanned_strategy")
    private UnmannedStrategy unmannedStrategy;

}
