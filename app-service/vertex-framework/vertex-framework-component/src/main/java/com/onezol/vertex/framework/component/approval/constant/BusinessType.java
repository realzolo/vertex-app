package com.onezol.vertex.framework.component.approval.constant;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.onezol.vertex.framework.common.annotation.EnumDictionary;
import com.onezol.vertex.framework.common.constant.enumeration.StandardEnumeration;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Schema(name = "业务类型")
@Getter
@AllArgsConstructor
@EnumDictionary(name = "业务类型", value = "business_type")
public enum BusinessType implements StandardEnumeration<String> {

    ANNOUNCEMENT_PUBLISH("公告发布", "ANNOUNCEMENT_PUBLISH");

    private final String name;

    @EnumValue
    private final String value;

}
