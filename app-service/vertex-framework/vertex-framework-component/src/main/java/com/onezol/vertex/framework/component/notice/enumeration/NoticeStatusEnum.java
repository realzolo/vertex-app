package com.onezol.vertex.framework.component.notice.enumeration;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.onezol.vertex.framework.common.annotation.EnumDictionary;
import com.onezol.vertex.framework.common.constant.ColorConstants;
import com.onezol.vertex.framework.common.constant.enumeration.Enumeration;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Schema(name = "通知公告状态")
@Getter
@RequiredArgsConstructor
@EnumDictionary(name = "通知公告状态", value = "notice_status")
public enum NoticeStatusEnum implements Enumeration<Integer> {

    PENDING("待发布", 0, ColorConstants.COLOR_DEFAULT),

    PUBLISHED("已发布", 1, ColorConstants.COLOR_SUCCESS),

    EXPIRED("已过期", 2, ColorConstants.COLOR_WARNING);

    private final String name;

    @EnumValue
    private final Integer value;

    private final String color;

}
