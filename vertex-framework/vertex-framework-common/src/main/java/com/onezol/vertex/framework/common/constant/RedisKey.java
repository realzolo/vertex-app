package com.onezol.vertex.framework.common.constant;

/**
 * Redis常量
 */
public record RedisKey() {
    /**
     * 用户信息前缀
     */
    public static String ONLINE_USERID_SET = "online_userid_set";
    public static String ONLINE_USER = "online_userinfo:";

    /**
     * 字典
     */
    public static String DICTIONARY = "dictionary";

    /**
     * 枚举
     */
    public static String ENUM = "enum";
}
