package com.onezol.vertex.framework.common.util;

import com.onezol.vertex.framework.common.constant.enumeration.Enumeration;

import java.util.Objects;

/**
 * 枚举工具类
 */
public final class EnumUtils {

    private EnumUtils() {
        throw new IllegalStateException("Utility class cannot be instantiated");
    }

    public static <T extends Enumeration<?>> T getEnumByValue(Class<T> enumClass, T value) {
        T[] enumConstants = enumClass.getEnumConstants();
        for (T enumConstant : enumConstants) {
            if (Objects.equals(enumConstant.getValue(), value.toString())) {
                return enumConstant;
            }
        }
        return null;
    }

}
