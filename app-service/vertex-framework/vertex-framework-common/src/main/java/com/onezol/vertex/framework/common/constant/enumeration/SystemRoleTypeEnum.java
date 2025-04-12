package com.onezol.vertex.framework.common.constant.enumeration;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Schema(name = "系统角色类型")
@Getter
@RequiredArgsConstructor
public enum SystemRoleTypeEnum implements Enumeration<String> {

    SUPER("super", "super"),

    ADMIN("admin", "admin");


    private final String name;

    private final String value;

}
