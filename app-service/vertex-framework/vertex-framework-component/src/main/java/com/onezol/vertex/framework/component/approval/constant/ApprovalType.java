package com.onezol.vertex.framework.component.approval.constant;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.onezol.vertex.framework.common.annotation.EnumDictionary;
import com.onezol.vertex.framework.common.constant.enumeration.StandardEnumeration;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Schema(name = "审批方式")
@Getter
@AllArgsConstructor
@EnumDictionary(name = "审批方式", value = "approval_type")
public enum ApprovalType implements StandardEnumeration<Integer> {

    SERIAL("串行", 0),
    COUNTERSIGN("会签", 1),
    PARALLEL_SIGN("或签", 2);

    private final String name;

    @EnumValue
    private final Integer value;

}