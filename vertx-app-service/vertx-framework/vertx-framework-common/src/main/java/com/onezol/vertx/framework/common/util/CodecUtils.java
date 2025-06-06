package com.onezol.vertx.framework.common.util;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

/**
 * 编码工具
 */
public final class CodecUtils {

    private CodecUtils() {
        throw new IllegalStateException("Utility class cannot be instantiated");
    }

    /**
     * Base64编码
     *
     * @param str 待编码字符串
     * @return 编码后字符串
     */
    public static String encodeBase64(String str) {
        return Base64.getEncoder().encodeToString(str.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * Base64解码
     *
     * @param str 待解码字符串
     * @return 解码后字符串
     */
    public static String decodeBase64(String str) {
        return new String(Base64.getDecoder().decode(str), StandardCharsets.UTF_8);
    }

}

