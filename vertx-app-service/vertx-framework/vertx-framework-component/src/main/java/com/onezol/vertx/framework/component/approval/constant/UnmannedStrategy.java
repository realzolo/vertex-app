package com.onezol.vertx.framework.component.approval.constant;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.onezol.vertx.framework.common.annotation.EnumDictionary;
import com.onezol.vertx.framework.common.constant.enumeration.StandardEnumeration;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Schema(name = "无审批人策略")
@Getter
@AllArgsConstructor
@EnumDictionary(name = "无审批人策略", value = "unmanned_strategy")
public enum UnmannedStrategy implements StandardEnumeration<Integer> {

    AUTOMATIC_PASS("自动通过", 0),

    FORWARD_ADMINISTRATOR("转交管理员", 1),

    DESIGNATE_USER("指定人员", 2);

    private final String name;

    @EnumValue
    private final Integer value;

}
