package com.onezol.vertex.framework.common.constant;

/**
 * Redis常量
 */
public record RedisKey() {
    /**
     * 用户信息前缀
     */
    public static String USERID_SET = "uid_set";
    public static String USER = "user:";

    /**
     * 字典
     */
    public static String DICTIONARY = "dictionary";

    /**
     * 枚举
     */
    public static String ENUM = "enum";
}
