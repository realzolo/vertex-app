package com.onezol.vertx.framework.component.approval.constant;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.onezol.vertx.framework.common.annotation.EnumDictionary;
import com.onezol.vertx.framework.common.constant.enumeration.StandardEnumeration;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Schema(name = "候选人选择类型")
@Getter
@AllArgsConstructor
@EnumDictionary(name = "候选人选择类型", value = "candidate_selection_type")
public enum CandidateSelectionType implements StandardEnumeration<Integer> {

    SELECT_ONE("自选一人", 0),
    SELECT_MULTIPLE("自选多人", 1);

    private final String name;

    @EnumValue
    private final Integer value;

}