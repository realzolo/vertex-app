package com.onezol.vertex.framework.support.support;

import java.util.Objects;

public final class RedisKeyHelper {

    /**
     * 生成Redis Key
     *
     * @param templateKey 模板Key，形如 "user:token:{userId}"
     * @param args        参数
     * @return 生成的Redis Key
     */
    public static String buildRedisKey(String templateKey, String... args) {
        for (String arg : args) {
            String placeholderString = getNextPlaceholderString(templateKey);
            if (Objects.isNull(placeholderString)) {
                throw new IllegalArgumentException("生成Redis Key失败！参数数量大于参数占位符数量");
            }
            templateKey = templateKey.replace(placeholderString, arg);
        }
        if (templateKey.indexOf('{') != -1) {
            throw new IllegalArgumentException("生成Redis Key失败！参数占位符数量大于参数数量");
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
     * @param templateKey Redis模板Key
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

