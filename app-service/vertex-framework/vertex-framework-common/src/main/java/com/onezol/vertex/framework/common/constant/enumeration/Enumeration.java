package com.onezol.vertex.framework.common.constant.enumeration;

import java.io.Serializable;
import java.util.Objects;

/**
 * 枚举接口, 提供枚举类的基础方法
 */
public interface Enumeration<T extends Serializable> {

    static <T extends Enumeration<?>> T getEnumByValue(Class<T> enumClass, T value) {
        T[] enumConstants = enumClass.getEnumConstants();
        for (T enumConstant : enumConstants) {
            if (Objects.equals(enumConstant.getValue(), value.toString())) {
                return enumConstant;
            }
        }
        return null;
    }

    default String getName() {
        return null;
    }

    T getValue();

    default String getColor() {
        return null;
    }

    default String getDescription() {
        return null;
    }
}
