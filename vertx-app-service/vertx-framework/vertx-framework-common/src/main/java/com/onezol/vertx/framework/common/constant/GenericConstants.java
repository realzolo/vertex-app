package com.onezol.vertx.framework.common.constant;

/**
 * 通用常量类
 */
@SuppressWarnings("ALL")
public final class GenericConstants {

    private GenericConstants() {
    }

    /**  http协议 */
    public static String PROTOCOL_HTTP = "http://";

    /** * https协议 */
    public static String PROTOCOL_HTTPS = "https://";

    /** * UNKNOWN */
    public static String UNKNOWN = "unknown";

    /** * Authorization header 前缀 */
    public static String AUTHORIZATION_HEADER = "Bearer ";

    /** 全部权限 */
    public static String ALL_PERMISSION = "*:*:*";

    /** 根级对象父级ID */
    public static Long ROOT_OBJECT_PARENT_ID = 0L;

    /** 默认时区  */
    public static String DEFAULT_TIME_ZONE = "Asia/Shanghai";
}
