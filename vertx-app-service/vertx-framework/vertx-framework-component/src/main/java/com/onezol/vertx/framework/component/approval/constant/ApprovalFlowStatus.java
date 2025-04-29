package com.onezol.vertx.framework.component.approval.constant;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.onezol.vertx.framework.common.annotation.EnumDictionary;
import com.onezol.vertx.framework.common.constant.enumeration.StandardEnumeration;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Schema(name = "审批流程状态")
@Getter
@AllArgsConstructor
@EnumDictionary(name = "审批流程状态", value = "approval_flow_status")
public enum ApprovalFlowStatus implements StandardEnumeration<Integer> {

    DRAFT("暂存", 0),
    PENDING("审批中", 0),
    APPROVED("通过", 1),
    REJECTED("驳回", 2),
    CANCELLED("撤回", 3);

    private final String name;

    @EnumValue
    private final Integer value;

}
