package com.onezol.vertex.framework.common.constant;

/**
 * 常量类
 */
@SuppressWarnings("ALL")
public record Constants() {

    /**
     * http协议
     */
    public static String PROTOCOL_HTTP = "http://";

    /**
     * https协议
     */
    public static String PROTOCOL_HTTPS = "https://";

    /**
     * NULL值标识符
     */
    public static String SYMBOL_NULL = "::SYMBOL::4CCA07368BEB40DA0DB2101421C9E46B::";

    /**
     * 默认页大小
     */
    public static int DEFAULT_PAGE_SIZE = 10;

    /**
     * 最大页大小
     */
    public static int MAX_PAGE_SIZE = 200;

    /**
     * UNKNOWN
     */
    public static String UNKNOWN = "unknown";

    /**
     * Authorization header 前缀
     */
    public static String AUTHORIZATION_HEADER = "Bearer ";
}
