package com.onezol.vertex.framework.common.constant.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum UserGender implements Enum{

    MALE(0, "男"),

    FEMALE(1, "女");

    private final int code;

    private final String value;

}
