package com.onezol.vertex.framework.common.util;

public final class ValidationUtils {
    // 邮箱正则表达式
    public static final String PATTERN_EMAIL = "^[a-zA-Z0-9_-]+@[a-zA-Z0-9_-]+(\\.[a-zA-Z0-9_-]+)+$";

    // 手机号正则表达式
    public static final String PATTERN_MOBILE_PHONE = "^(?:(?:\\+|00)86)?1(?:3\\d|4[0,14-9]|5[0-3,5-9]|6[2,5-7]|7[0-8]|8\\d|9[0-3,5-9])\\d{8}$";
    // URL正则表达式
    public static final String PATTERN_URL = "^(https?|ftp|file)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]";

    /**
     * 判断是否是邮箱
     *
     * @param email 邮箱
     */
    public static boolean isEmail(String email) {
        email = email == null ? "" : email.trim();
        return email.matches(PATTERN_EMAIL);
    }

    /**
     * 判断是否是手机号
     *
     * @param phone 手机号
     */
    public static boolean isMobilePhone(String phone) {
        phone = phone == null ? "" : phone.trim();
        return phone.matches(PATTERN_MOBILE_PHONE);
    }

    /**
     * 判断是否是URL
     *
     * @param url URL
     */
    public static boolean isURL(String url) {
        url = url == null ? "" : url.trim();
        return url.matches(PATTERN_URL);
    }
}
