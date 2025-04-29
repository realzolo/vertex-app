package com.onezol.vertx.framework.component.approval.constant;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.onezol.vertx.framework.common.annotation.EnumDictionary;
import com.onezol.vertx.framework.common.constant.enumeration.StandardEnumeration;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Schema(name = "候选人策略")
@Getter
@AllArgsConstructor
@EnumDictionary(name = "候选人策略", value = "candidate_strategy")
public enum CandidateStrategy implements StandardEnumeration<Integer> {

    DESIGNATE_USER("指定成员", 0),
    DESIGNATE_ROLE("指定角色", 1),
    INITIATOR("发起人自己", 2),
    SELECTION("发起人自选", 3);

    private final String name;

    @EnumValue
    private final Integer value;

}
