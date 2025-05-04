package com.onezol.vertx.framework.extension.schedule.enums;

import com.onezol.vertx.framework.common.annotation.EnumDictionary;
import com.onezol.vertx.framework.common.constant.ColorConstants;
import com.onezol.vertx.framework.common.constant.enumeration.StandardEnumeration;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Schema(name = "任务类型")
@Getter
@RequiredArgsConstructor
@EnumDictionary(name = "任务类型", value = "job_task_type")
public enum JobTaskType implements StandardEnumeration<Integer> {

    CLUSTER("集群", 1, ColorConstants.COLOR_PRIMARY),

    BROADCAST("广播", 2, ColorConstants.COLOR_PRIMARY),

    SLICE("静态切片", 3, ColorConstants.COLOR_PRIMARY);

    private final String name;

    private final Integer value;

    private final String color;

}
