package com.onezol.vertex.framework.security.api.enumeration;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.onezol.vertex.framework.common.annotation.EnumDictionary;
import com.onezol.vertex.framework.common.constant.enumeration.Enumeration;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Schema(name = "登录类型")
@Getter
@RequiredArgsConstructor
@EnumDictionary(name = "登录类型", value = "login_type")
public enum LoginTypeEnum implements Enumeration<String> {

    PBA("用户名密码登录", "PBA"),

    EMAIL("邮箱登录", "EMAIL"),

    SMS("短信登录", "SMS");

    private final String name;

    @EnumValue
    private final String value;

}
