package com.onezol.vertx.framework.common.constant.enumeration;

import com.onezol.vertx.framework.common.annotation.EnumDictionary;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Schema(name = "登录状态")
@Getter
@AllArgsConstructor
@EnumDictionary(name = "登录状态", value = "login_status")
public enum LoginStatus implements StandardEnumeration<Integer> {

    SUCCESS("登录成功", 0),

    PASSWORD_ERROR("密码错误", 1),

    CAPTCHA_ERROR("验证码错误", 2),

    ACCOUNT_DISABLED("账户禁用", 3),

    ACCOUNT_LOCKED("账户锁定", 4);


    private final String name;

    private final Integer value;
}
