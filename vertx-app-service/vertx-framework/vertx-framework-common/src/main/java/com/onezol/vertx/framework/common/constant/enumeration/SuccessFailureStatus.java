package com.onezol.vertx.framework.common.constant.enumeration;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.onezol.vertx.framework.common.annotation.EnumDictionary;
import com.onezol.vertx.framework.common.constant.ColorConstants;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Schema(name = "成功/失败状态")
@Getter
@RequiredArgsConstructor
@EnumDictionary(name = "成功/失败状态", value = "success_failure_status")
public enum SuccessFailureStatus implements StandardEnumeration<Integer> {

    SUCCESS("成功", 1, ColorConstants.COLOR_SUCCESS),

    FAILURE("失败", 0, ColorConstants.COLOR_ERROR);

    private final String name;

    @EnumValue
    private final Integer value;

    private final String color;

}
