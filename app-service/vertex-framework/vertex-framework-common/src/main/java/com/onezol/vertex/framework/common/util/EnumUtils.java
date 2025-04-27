package com.onezol.vertex.framework.common.util;

import com.onezol.vertex.framework.common.constant.enumeration.StandardEnumeration;

import java.util.Objects;

/**
 * 枚举工具类
 */
public final class EnumUtils {

    private EnumUtils() {
        throw new IllegalStateException("Utility class cannot be instantiated");
    }

    public static <T extends StandardEnumeration<?>> T getEnumByValue(Class<T> enumClass, Object value) {
        T[] enumConstants = enumClass.getEnumConstants();
        for (T enumConstant : enumConstants) {
            if (Objects.equals(enumConstant.getValue(), value)) {
                return enumConstant;
            }
        }
        return null;
    }

}
