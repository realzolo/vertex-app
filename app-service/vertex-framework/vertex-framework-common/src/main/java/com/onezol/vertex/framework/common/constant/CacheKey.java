package com.onezol.vertex.framework.common.constant;

/**
 * 缓存Key常量模板
 */
public record CacheKey() {
    /**
     * 用户Token
     */
    public static final String USER_TOKEN = "user:token:{userId}";

    /**
     * 用户信息
     */
    public static final String USER_INFO = "user:info:{userId}";

    /**
     * 验证码
     */
    public static final String CAPTCHA = "captcha:{uuid}";

    /**
     * 字典
     */
    public static final String DICTIONARY = "dictionary";
}
