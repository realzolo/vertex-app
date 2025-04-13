package com.onezol.vertex.framework.security.api.enumeration;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.onezol.vertex.framework.common.annotation.EnumDictionary;
import com.onezol.vertex.framework.common.constant.enumeration.Enumeration;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Schema(name = "权限类型")
@Getter
@RequiredArgsConstructor
@EnumDictionary(name = "权限类型", value = "permission_type")
public enum PermissionTypeEnum implements Enumeration<Integer> {

    /**
     * 目录
     */
    DIR("目录", 1),

    /**
     * 菜单
     */
    MENU("菜单", 2),

    /**
     * 按钮
     */
    BUTTON("按钮/权限", 3);

    private final String name;

    @EnumValue
    private final Integer value;

}
