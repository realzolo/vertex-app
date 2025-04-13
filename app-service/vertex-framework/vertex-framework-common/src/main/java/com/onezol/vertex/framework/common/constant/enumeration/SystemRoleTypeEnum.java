package com.onezol.vertex.framework.common.constant.enumeration;

import com.onezol.vertex.framework.common.annotation.EnumDictionary;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Schema(name = "系统角色类型")
@Getter
@RequiredArgsConstructor
@EnumDictionary(name = "系统角色类型", value = "system_role_type")
public enum SystemRoleTypeEnum implements Enumeration<String> {

    SUPER("超级管理员", "super"),

    ADMIN("系统管理员", "admin");


    private final String name;

    private final String value;

}
