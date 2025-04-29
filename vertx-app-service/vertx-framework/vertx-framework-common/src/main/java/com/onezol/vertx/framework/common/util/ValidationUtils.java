package com.onezol.vertx.framework.common.util;

/**
 * 校验工具类
 */
public final class ValidationUtils {

    private ValidationUtils() {
        throw new IllegalStateException("Utility class cannot be instantiated");
    }

    // 邮箱正则表达式
    public static final String PATTERN_EMAIL = "^[a-zA-Z0-9_-]+@[a-zA-Z0-9_-]+(\\.[a-zA-Z0-9_-]+)+$";

    // 手机号正则表达式
    public static final String PATTERN_MOBILE_PHONE = "^(?:(?:\\+|00)86)?1(?:3\\d|4[0,14-9]|5[0-3,5-9]|6[2,5-7]|7[0-8]|8\\d|9[0-3,5-9])\\d{8}$";

    // URL正则表达式
    public static final String PATTERN_URL = "^(https?|ftp|file)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]";

    /**
     * 校验邮箱格式是否正确
     *
     * @param email 邮箱
     */
    public static boolean validateEmail(String email) {
        email = email == null ? "" : email;
        return email.matches(PATTERN_EMAIL);
    }

    /**
     * 校验手机号格式是否正确
     *
     * @param phone 手机号
     */
    public static boolean validateMobilePhone(String phone) {
        phone = phone == null ? "" : phone;
        return phone.matches(PATTERN_MOBILE_PHONE);
    }

    /**
     * 校验URL格式是否正确
     *
     * @param url URL
     */
    public static boolean validateURL(String url) {
        url = url == null ? "" : url;
        return url.matches(PATTERN_URL);
    }

}
