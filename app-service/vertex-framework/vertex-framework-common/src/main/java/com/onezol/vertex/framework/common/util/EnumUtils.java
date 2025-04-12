package com.onezol.vertex.framework.common.util;

import com.onezol.vertex.framework.common.constant.enumeration.Enumeration;
import com.onezol.vertex.framework.common.constant.enumeration.SystemRoleTypeEnum;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Objects;

/**
 * 枚举工具类
 */
public final class EnumUtils {

    private EnumUtils() {
        throw new IllegalStateException("Utility class cannot be instantiated");
    }

    public static <T extends Enumeration<?>> T getEnumByValue(Class<T> enumClass, Object value) {
        T[] enumConstants = enumClass.getEnumConstants();
        for (T enumConstant : enumConstants) {
            if (Objects.equals(enumConstant.getValue(), String.valueOf(value))) {
                return enumConstant;
            }
        }
        return null;
    }

}
