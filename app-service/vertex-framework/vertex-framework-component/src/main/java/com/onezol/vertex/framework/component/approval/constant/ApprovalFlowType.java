package com.onezol.vertex.framework.component.approval.constant;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.onezol.vertex.framework.common.annotation.EnumDictionary;
import com.onezol.vertex.framework.common.constant.enumeration.StandardEnumeration;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Schema(name = "审批流程类型")
@Getter
@AllArgsConstructor
@EnumDictionary(name = "审批流程类型", value = "approval_flow_type")
public enum ApprovalFlowType implements StandardEnumeration<Integer> {

    GENERIC_APPROVAL("通用审批", 0);

    private final String name;

    @EnumValue
    private final Integer value;

}
