package com.onezol.vertex.framework.component.approval.constant;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.onezol.vertex.framework.common.annotation.EnumDictionary;
import com.onezol.vertex.framework.common.constant.enumeration.StandardEnumeration;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Schema(name = "审批流程状态")
@Getter
@AllArgsConstructor
@EnumDictionary(name = "审批流程状态", value = "approval_flow_status")
public enum ApprovalFlowStatus implements StandardEnumeration<Integer> {

    PENDING("待审批", 0),
    APPROVED("已通过", 1),
    REJECTED("已驳回", 2),
    CANCELLED("已撤回", 3);

    private final String name;

    @EnumValue
    private final Integer value;

}
