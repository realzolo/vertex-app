package com.onezol.vertx.framework.security.api.enumeration;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.onezol.vertx.framework.common.annotation.EnumDictionary;
import com.onezol.vertx.framework.common.constant.enumeration.StandardEnumeration;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Schema(name = "权限类型")
@Getter
@RequiredArgsConstructor
@EnumDictionary(name = "权限类型", value = "permission_type")
public enum PermissionTypeEnum implements StandardEnumeration<Integer> {

    DIR("目录", 1),

    MENU("菜单", 2),

    BUTTON("按钮/权限", 3);

    private final String name;

    @EnumValue
    private final Integer value;

}
