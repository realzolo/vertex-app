package com.onezol.vertx.framework.extension.schedule.enums;

import com.onezol.vertx.framework.common.annotation.EnumDictionary;
import com.onezol.vertx.framework.common.constant.ColorConstants;
import com.onezol.vertx.framework.common.constant.enumeration.StandardEnumeration;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Schema(name = "任务执行状态")
@Getter
@RequiredArgsConstructor
@EnumDictionary(name = "任务执行状态", value = "job_execute_status")
public enum JobExecuteStatus implements StandardEnumeration<Integer> {

    WAITING("待处理", 1, ColorConstants.COLOR_PRIMARY),

    RUNNING("运行中", 2, ColorConstants.COLOR_WARNING),

    SUCCEEDED("成功", 3, ColorConstants.COLOR_SUCCESS),

    FAILED("已失败", 4, ColorConstants.COLOR_ERROR),

    STOPPED("已停止", 5, ColorConstants.COLOR_ERROR),

    CANCELED("已取消", 6, ColorConstants.COLOR_DEFAULT);

    private final String name;

    private final Integer value;

    private final String color;

}
