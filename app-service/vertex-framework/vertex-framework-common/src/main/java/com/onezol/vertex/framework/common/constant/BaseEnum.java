package com.onezol.vertex.framework.common.constant;

import java.io.Serializable;

public interface BaseEnum<T extends Serializable> {
    T getValue();

    String getDescription();

    default String getColor() {
        return null;
    }
}