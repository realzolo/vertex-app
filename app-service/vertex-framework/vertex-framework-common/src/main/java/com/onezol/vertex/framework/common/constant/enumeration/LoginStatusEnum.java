package com.onezol.vertex.framework.common.constant.enumeration;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 登录状态枚举
 */
@Getter
@AllArgsConstructor
public enum LoginStatusEnum implements Enumeration<Integer> {

    SUCCESS("登录成功", 0),

    PASSWORD_ERROR("密码错误", 1),

    CAPTCHA_ERROR("验证码错误", 2),

    ACCOUNT_DISABLED("账户禁用", 3),

    ACCOUNT_LOCKED("账户锁定", 4);


    private final String name;

    private final Integer value;
}
