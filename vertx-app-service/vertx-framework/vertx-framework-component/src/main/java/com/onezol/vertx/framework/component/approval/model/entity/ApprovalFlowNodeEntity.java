package com.onezol.vertx.framework.component.approval.model.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.onezol.vertx.framework.common.model.entity.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Schema(description = "审批流程节点")
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("approval_flow_node")
public class ApprovalFlowNodeEntity extends BaseEntity {

    @Schema(description = "流程模板ID")
    @TableField("template_id")
    private Long templateId;

    @Schema(description = "节点ID")
    @TableField("node_id")
    private String nodeId;

    @Schema(description = "节点名称")
    @TableField("label")
    private String label;

    @Schema(description = "节点类型")
    @TableField("type")
    private String type;

    @Schema(description = "上一个节点ID")
    @TableField("prev_node_id")
    private String prevNodeId;

    @Schema(description = "下一个节点ID")
    @TableField("next_node_id")
    private String nextNodeId;

}
