package com.onezol.vertex.framework.component.notice.enumeration;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.onezol.vertex.framework.common.annotation.EnumDictionary;
import com.onezol.vertex.framework.common.constant.enumeration.StandardEnumeration;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Schema(name = "通知公告类型")
@Getter
@RequiredArgsConstructor
@EnumDictionary(name = "通知公告类型", value = "notice_type")
public enum NoticeType implements StandardEnumeration<Integer> {

    SUPER("系统通知", 0);

    private final String name;

    @EnumValue
    private final Integer value;

}
