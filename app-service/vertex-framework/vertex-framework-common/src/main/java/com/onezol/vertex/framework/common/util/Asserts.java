package com.onezol.vertex.framework.common.util;

import com.onezol.vertex.framework.common.constant.enumeration.ServiceStatusEnum;
import com.onezol.vertex.framework.common.exception.RuntimeServiceException;
import com.onezol.vertex.framework.common.exception.ServiceException;

import java.util.Collection;
import java.util.Collections;

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
            throw generateException(message);
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
            throw generateException(message);
        }
    }

    /**
     * 判断集合是否为空
     * @param collection 集合
     * @param message 错误信息
     */
    public static void notEmpty(Collection<?> collection, String message) {
        if (collection == null || collection.isEmpty()) {
            throw generateException(message);
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
            throw generateException(message);
        }
    }

    public static void notNull(Object object) {
        notNull(object, null);
    }

    public static void notEmpty(String str) {
        notEmpty(str, null);
    }

    public static void notEmpty(Collection<?> collection) {
        notEmpty(collection, null);
    }

    public static void notBlank(String str) {
        notBlank(str, null);
    }


    private static ServiceException generateException() {
        return new RuntimeServiceException(ServiceStatusEnum.BAD_REQUEST);
    }

    private static ServiceException generateException(String message) {
        return new RuntimeServiceException(ServiceStatusEnum.BAD_REQUEST, message);
    }

}
