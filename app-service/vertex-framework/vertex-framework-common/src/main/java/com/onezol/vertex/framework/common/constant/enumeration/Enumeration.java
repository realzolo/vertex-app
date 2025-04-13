package com.onezol.vertex.framework.common.constant.enumeration;

import java.io.Serializable;

/**
 * 枚举接口, 提供枚举类的基础方法
 */
public interface Enumeration<T extends Serializable> {

    String getName();

    T getValue();

    default String getColor() {
        return "";
    }

}
