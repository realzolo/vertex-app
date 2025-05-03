package com.onezol.vertx.framework.security.api.enumeration;

import com.onezol.vertx.framework.common.constant.enumeration.StandardEnumeration;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Schema(name = "系统内置角色")
@Getter
@RequiredArgsConstructor
public enum BuiltinRole implements StandardEnumeration<String> {

    SUPER("超级管理员", "super"),

    ADMIN("系统管理员", "admin");


    private final String name;

    private final String value;

}
