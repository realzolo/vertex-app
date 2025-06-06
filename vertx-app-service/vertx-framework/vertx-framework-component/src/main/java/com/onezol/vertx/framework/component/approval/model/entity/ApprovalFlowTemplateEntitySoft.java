package com.onezol.vertx.framework.component.approval.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.onezol.vertx.framework.common.skeleton.model.SoftDeletableEntity;
import com.onezol.vertx.framework.component.approval.constant.ApprovalFlowType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("app_approval_flow_template")
public class ApprovalFlowTemplateEntitySoft extends SoftDeletableEntity {

    @Schema(name = "流程名称")
    private String name;

    @Schema(name = "流程类型")
    private ApprovalFlowType type;

    @Schema(name = "流程内容")
    private String content;

    @Schema(name = "流程状态")
    private Integer status;

    @Schema(name = "流程备注")
    private String remark;

}
