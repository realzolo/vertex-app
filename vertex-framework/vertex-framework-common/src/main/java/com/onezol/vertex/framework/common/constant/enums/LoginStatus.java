package com.onezol.vertex.framework.common.constant.enums;

public enum LoginStatus implements Enum {
    SUCCESS(0, "登录成功"),
    PASSWORD_ERROR(1, "密码错误"),
    CAPTCHA_ERROR(2, "验证码错误"),
    ACCOUNT_DISABLED(3, "账户禁用"),
    ACCOUNT_LOCKED(4, "账户锁定");

    private final int code;
    private final String value;

    LoginStatus(int code, String value) {
        this.code = code;
        this.value = value;
    }

    @Override
    public int getCode() {
        return code;
    }

    @Override
    public String getValue() {
        return value;
    }
}
