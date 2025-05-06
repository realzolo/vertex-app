package com.onezol.vertx.framework.component.approval.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.onezol.vertx.framework.common.skeleton.model.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("app_approval_flow_binding_relation")
public class ApprovalFlowBindingRelationEntity extends BaseEntity {

    @Schema(name = "流程模板ID")
    private Long flowTemplateId;

    @Schema(name = "业务类型编码")
    private String businessTypeCode;

}
