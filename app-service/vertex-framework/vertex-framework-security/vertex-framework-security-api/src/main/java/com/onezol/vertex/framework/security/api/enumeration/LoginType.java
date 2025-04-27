package com.onezol.vertex.framework.security.api.enumeration;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.onezol.vertex.framework.common.annotation.EnumDictionary;
import com.onezol.vertex.framework.common.constant.enumeration.StandardEnumeration;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Schema(name = "登录类型")
@Getter
@RequiredArgsConstructor
@EnumDictionary(name = "登录类型", value = "login_type")
public enum LoginType implements StandardEnumeration<String> {

    UP("密码", "UP"),

    EMAIL("邮箱", "EMAIL"),

    SMS("短信", "SMS");

    private final String name;

    @EnumValue
    private final String value;

}
