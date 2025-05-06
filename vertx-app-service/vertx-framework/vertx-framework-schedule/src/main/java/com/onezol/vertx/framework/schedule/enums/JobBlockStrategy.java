package com.onezol.vertx.framework.schedule.enums;

import com.onezol.vertx.framework.common.annotation.EnumDictionary;
import com.onezol.vertx.framework.common.constant.enumeration.StandardEnumeration;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Schema(name = "任务阻塞策略")
@Getter
@RequiredArgsConstructor
@EnumDictionary(name = "任务阻塞策略", value = "job_block_strategy")
public enum JobBlockStrategy implements StandardEnumeration<Integer> {

    DISCARD("丢弃", 1),

    COVER("覆盖", 2),

    PARALLEL("并行", 3);

    private final String name;

    private final Integer value;

}
