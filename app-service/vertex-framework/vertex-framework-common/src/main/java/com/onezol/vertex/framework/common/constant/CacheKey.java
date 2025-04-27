package com.onezol.vertex.framework.common.constant;

/**
 * 缓存Key常量模板
 */
public final class CacheKey {

    private CacheKey() {
    }

    /**
     * 用户Token
     */
    public static final String USER_TOKEN = "user:token:{userId}";

    /**
     * 用户信息
     */
    public static final String USER_INFO = "user:info:{userId}";

    /**
     * 用户信息
     */
    public static final String ONLINE_USER = "user:online_users";

    /**
     * 验证码(UP方式)
     */
    public static final String VC_UP = "vc:up:{fingerprint}";

    /**
     * 验证码(Email方式)
     */
    public static final String VC_EMAIL = "vc:email:{email}";

    /**
     * 字典
     */
    public static final String DICTIONARY = "dictionary";
}
