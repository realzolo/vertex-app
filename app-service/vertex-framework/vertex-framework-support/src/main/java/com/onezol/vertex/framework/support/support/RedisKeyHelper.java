package com.onezol.vertex.framework.support.support;

import java.util.Objects;

/**
 * Redis Key生成工具
 */
public final class RedisKeyHelper {

    private RedisKeyHelper() {
        throw new IllegalStateException("Utility class cannot be instantiated");
    }

    /**
     * 生成缓存 Key
     *
     * @param templateKey 模板Key，形如 "user:token:{userId}"
     * @param args        参数
     * @return 生成的Cache Key
     */
    public static String buildCacheKey(String templateKey, String... args) {
        for (String arg : args) {
            String placeholderString = getNextPlaceholderString(templateKey);
            if (Objects.isNull(placeholderString)) {
                throw new IllegalArgumentException("生成Cache Key失败！参数数量大于参数占位符数量");
            }
            templateKey = templateKey.replace(placeholderString, arg);
        }
        if (templateKey.indexOf('{') != -1) {
            throw new IllegalArgumentException("生成Cache Key失败！参数占位符数量大于参数数量");
        }
        return templateKey;
    }

    // 获取通配符Key. 如："user:token:{userId}" 则返回 "user:token:*"
    public static String getWildcardKey(String key) {
        return key.substring(0, key.indexOf("{")) + "*";
    }

    /**
     * 获取下一个占位符
     *
     * @param templateKey 缓存模板Key
     */
    private static String getNextPlaceholderString(String templateKey) {
        int startIndex = templateKey.indexOf('{');
        int endIndex = templateKey.indexOf('}');
        if (startIndex == -1 || endIndex == -1) {
            return null;
        }
        return templateKey.substring(startIndex, endIndex + 1);
    }

}

