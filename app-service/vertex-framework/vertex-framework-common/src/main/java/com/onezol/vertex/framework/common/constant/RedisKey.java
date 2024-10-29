package com.onezol.vertex.framework.common.constant;

/**
 * Redis常量
 */
public record RedisKey() {
    /**
     * 用户Token
     */
    public static final String USER_TOKEN = "user:token:{userId}";

    /**
     * 用户信息
     */
    public static final String USER_INFO = "user:info:{userId}";
}
