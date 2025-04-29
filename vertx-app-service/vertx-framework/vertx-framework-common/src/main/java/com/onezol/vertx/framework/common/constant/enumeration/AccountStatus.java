package com.onezol.vertx.framework.common.constant.enumeration;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.onezol.vertx.framework.common.annotation.EnumDictionary;
import com.onezol.vertx.framework.common.constant.ColorConstants;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Schema(name = "账号状态")
@Getter
@AllArgsConstructor
@EnumDictionary(name = "账号状态", value = "account_status")
public enum AccountStatus implements StandardEnumeration<Integer> {

    ACTIVE("正常", 0, ColorConstants.COLOR_SUCCESS),

    LOCKED("锁定", 1, ColorConstants.COLOR_WARNING),

    DISABLED("禁用", 2, ColorConstants.COLOR_WARNING),

    EXPIRED("过期", 3, ColorConstants.COLOR_WARNING);


    private final String name;

    @EnumValue
    private final Integer value;

    private final String color;

}
