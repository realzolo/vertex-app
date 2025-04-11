package com.onezol.vertex.framework.common.constant.enumeration;

import java.io.Serializable;
import java.util.Objects;

/**
 * 枚举接口, 提供枚举类的基础方法
 */
public interface Enumeration<T extends Serializable> {

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
