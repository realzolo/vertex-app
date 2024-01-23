package com.onezol.vertex.framework.common.util;

public final class RegexUtils {
    // 邮箱正则表达式
    public static final String EMAIL_REGEX = "^[a-zA-Z0-9_-]+@[a-zA-Z0-9_-]+(\\.[a-zA-Z0-9_-]+)+$";

    // 手机号正则表达式
    public static final String PHONE_REGEX = "^1[3456789]\\d{9}$";

    public static boolean isEmail(String email) {
        email = email == null ? "" : email.trim();
        return email.matches(EMAIL_REGEX);
    }

    public static boolean isMobilePhone(String phone) {
        phone = phone == null ? "" : phone.trim();
        return phone.matches(PHONE_REGEX);
    }
}
