package com.onezol.vertx.framework.schedule.enums;

import com.onezol.vertx.framework.common.annotation.EnumDictionary;
import com.onezol.vertx.framework.common.constant.ColorConstants;
import com.onezol.vertx.framework.common.constant.enumeration.StandardEnumeration;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Schema(name = "任务状态")
@Getter
@RequiredArgsConstructor
@EnumDictionary(name = "任务状态", value = "job_status")
public enum JobStatus implements StandardEnumeration<Integer> {

    DISABLED("禁用", 0, ColorConstants.COLOR_ERROR),

    ENABLED("启用", 1, ColorConstants.COLOR_SUCCESS);

    private final String name;

    private final Integer value;

    private final String color;

}
