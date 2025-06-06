package com.onezol.vertx.framework.component.approval.model.dto;

import com.onezol.vertx.framework.common.skeleton.model.BaseDTO;
import com.onezol.vertx.framework.component.approval.constant.ApprovalResultType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = true)
public class ApprovalNodeRecord extends BaseDTO {

    @Schema(name = "流程实例ID")
    private Long instanceId;

    @Schema(name = "节点名称")
    private String nodeName;

    @Schema(name = "节点类型(审批/会签/或签等)")
    private String nodeType;

    @Schema(name = "审批人ID")
    private Long approverId;

    @Schema(name = "审批人姓名")
    private String approverName;

    @Schema(name = "审批结果(0-拒绝,1-同意)")
    private ApprovalResultType approveResult;

    @Schema(name = "审批意见")
    private String approveComment;

    @Schema(name = "审批时间")
    private LocalDateTime approveTime;
}
