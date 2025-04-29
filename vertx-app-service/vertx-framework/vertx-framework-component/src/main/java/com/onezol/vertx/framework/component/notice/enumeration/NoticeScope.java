package com.onezol.vertx.framework.component.notice.enumeration;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.onezol.vertx.framework.common.annotation.EnumDictionary;
import com.onezol.vertx.framework.common.constant.enumeration.StandardEnumeration;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Schema(name = "通知范围")
@Getter
@RequiredArgsConstructor
@EnumDictionary(name = "通知范围", value = "notice_scope")
public enum NoticeScope implements StandardEnumeration<Integer> {

    ALL_USERS("所有人", 0),

    SPECIFIC_USERS("指定用户", 1),

    // TODO: 暂未实现
    SPECIFIC_DEPARTMENTS("指定部门", 2),

    // TODO: 暂未实现
    SPECIFIC_ROLES("指定角色", 3);

    private final String name;

    @EnumValue
    private final Integer value;

}
