package com.onezol.vertex.framework.common.constant.enumeration;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.onezol.vertex.framework.common.constant.ColorConstants;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Schema(name = "启用/禁用状态")
@Getter
@RequiredArgsConstructor
public enum DisEnableStatusEnum implements Enumeration<Integer> {

    /**
     * 禁用
     */
    DISABLE("禁用", 0, ColorConstants.COLOR_ERROR),

    /**
     * 启用
     */
    ENABLE("启用", 1, ColorConstants.COLOR_SUCCESS);


    private final String name;

    @EnumValue
    private final Integer value;


    private final String color;

}
