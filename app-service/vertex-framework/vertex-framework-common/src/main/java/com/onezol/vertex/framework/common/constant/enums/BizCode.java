package com.onezol.vertex.framework.common.constant.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum BizCode implements Enum {

    USER_CODE(1000, "用户编码: 八位数字"),

    ;

    private final int code;

    private final String value;

}
