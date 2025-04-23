package com.onezol.vertex.framework.component.approval.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.onezol.vertex.framework.common.model.entity.BaseEntity;
import com.onezol.vertex.framework.component.approval.constant.ApprovalResultType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("vx_approval_node_record")
public class ApprovalNodeRecordEntity extends BaseEntity {

    @Schema(description = "流程实例ID")
    private Long instanceId;

    @Schema(description = "节点名称")
    private String nodeName;

    @Schema(description = "节点类型(审批/会签/或签等)")
    private String nodeType;

    @Schema(description = "审批人ID")
    private Long approverId;

    @Schema(description = "审批人姓名")
    private String approverName;

    @Schema(description = "审批结果(0-拒绝,1-同意)")
    private ApprovalResultType approveResult;

    @Schema(description = "审批意见")
    private String approveComment;

    @Schema(description = "审批时间")
    private LocalDateTime approveTime;

}
