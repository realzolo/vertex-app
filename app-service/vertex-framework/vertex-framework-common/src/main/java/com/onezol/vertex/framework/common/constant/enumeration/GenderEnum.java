package com.onezol.vertex.framework.common.constant.enumeration;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.onezol.vertex.framework.common.annotation.EnumDictionary;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Schema(name = "性别")
@Getter
@RequiredArgsConstructor
@EnumDictionary(name = "性别", value = "gender")
public enum GenderEnum implements Enumeration<Integer> {

    /**
     * 未知
     */
    UNKNOWN("未知", 0),

    /**
     * 男
     */
    MALE("男", 1),

    /**
     * 女
     */
    FEMALE("女", 2);


    private final String name;

    @EnumValue
    private final Integer value;

}
