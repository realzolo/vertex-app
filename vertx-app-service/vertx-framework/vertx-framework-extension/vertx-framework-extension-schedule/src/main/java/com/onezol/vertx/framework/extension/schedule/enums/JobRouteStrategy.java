package com.onezol.vertx.framework.extension.schedule.enums;

import com.onezol.vertx.framework.common.annotation.EnumDictionary;
import com.onezol.vertx.framework.common.constant.enumeration.StandardEnumeration;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Schema(name = "任务路由策略")
@Getter
@RequiredArgsConstructor
@EnumDictionary(name = "任务路由策略", value = "job_route_strategy")
public enum JobRouteStrategy implements StandardEnumeration<Integer> {

    POLLING("轮询", 4),

    RANDOM("随机", 2),

    HASH("一致性哈希", 1),

    LRU("LRU", 3);

    private final String name;

    private final Integer value;

}
