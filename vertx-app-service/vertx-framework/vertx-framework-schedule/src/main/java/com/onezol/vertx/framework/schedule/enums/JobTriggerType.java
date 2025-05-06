package com.onezol.vertx.framework.schedule.enums;

import com.onezol.vertx.framework.common.annotation.EnumDictionary;
import com.onezol.vertx.framework.common.constant.enumeration.StandardEnumeration;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Schema(name = "任务触发类型")
@Getter
@RequiredArgsConstructor
@EnumDictionary(name = "任务触发类型", value = "job_trigger_type")
public enum JobTriggerType implements StandardEnumeration<Integer> {

    FIXED_TIME("固定时间", 2),

    CRON("CRON", 3);

    private final String name;

    private final Integer value;

}
