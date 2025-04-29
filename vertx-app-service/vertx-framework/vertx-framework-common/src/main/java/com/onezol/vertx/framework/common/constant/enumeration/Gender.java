package com.onezol.vertx.framework.common.constant.enumeration;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.onezol.vertx.framework.common.annotation.EnumDictionary;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Schema(name = "性别")
@Getter
@RequiredArgsConstructor
@EnumDictionary(name = "性别", value = "gender")
public enum Gender implements StandardEnumeration<Integer> {

    UNKNOWN("未知", 0),

    MALE("男", 1),

    FEMALE("女", 2);


    private final String name;

    @EnumValue
    private final Integer value;

}
