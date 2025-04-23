package com.onezol.vertex.framework.component.approval.constant;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.onezol.vertex.framework.common.annotation.EnumDictionary;
import com.onezol.vertex.framework.common.constant.enumeration.StandardEnumeration;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Schema(name = "审批结果")
@Getter
@AllArgsConstructor
@EnumDictionary(name = "审批结果", value = "approval_result_type")
public enum ApprovalResultType implements StandardEnumeration<String> {

    AGREE("同意", "PASS"),
    REJECT("驳回", "REJECT");

    private final String name;

    @EnumValue
    private final String value;

}
