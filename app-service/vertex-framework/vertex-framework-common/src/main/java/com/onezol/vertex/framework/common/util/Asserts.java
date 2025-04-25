package com.onezol.vertex.framework.common.util;

import com.onezol.vertex.framework.common.exception.InvalidParameterException;

import java.util.Collection;

public final class Asserts {

    private Asserts() {
        throw new IllegalStateException("Utility class cannot be instantiated");
    }

    /**
     * 判断对象是否为空
     *
     * @param object  对象
     * @param message 错误信息
     */
    public static void notNull(Object object, String message) {
        if (object == null) {
            throw new InvalidParameterException(message);
        }
    }

    /**
     * 判断字符串是否为空
     *
     * @param str     字符串
     * @param message 错误信息
     */
    public static void notEmpty(String str, String message) {
        if (str == null || str.isEmpty()) {
            throw new InvalidParameterException(message);
        }
    }

    /**
     * 判断集合是否为空
     *
     * @param collection 集合
     * @param message    错误信息
     */
    public static void notEmpty(Collection<?> collection, String message) {
        if (collection == null || collection.isEmpty()) {
            throw new InvalidParameterException(message);
        }
    }

    /**
     * 判断字符串是否为空
     *
     * @param str     字符串
     * @param message 错误信息
     */
    public static void notBlank(String str, String message) {
        if (str == null || str.isBlank()) {
            throw new InvalidParameterException(message);
        }
    }

    public static void notNull(Object object) {
        notNull(object, null);
    }

}
