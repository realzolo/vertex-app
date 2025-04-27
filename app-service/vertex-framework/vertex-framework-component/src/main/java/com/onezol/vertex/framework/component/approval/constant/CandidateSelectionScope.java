package com.onezol.vertex.framework.component.approval.constant;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.onezol.vertex.framework.common.annotation.EnumDictionary;
import com.onezol.vertex.framework.common.constant.enumeration.StandardEnumeration;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Schema(name = "候选人选择范围")
@Getter
@AllArgsConstructor
@EnumDictionary(name = "候选人选择范围", value = "candidate_selection_scope")
public enum CandidateSelectionScope implements StandardEnumeration<Integer> {

    ALL("全公司/组织", 0),
    DESIGNATE_USER("指定成员", 1),
    DESIGNATE_ROLE("指定角色", 2);

    private final String name;

    @EnumValue
    private final Integer value;

}