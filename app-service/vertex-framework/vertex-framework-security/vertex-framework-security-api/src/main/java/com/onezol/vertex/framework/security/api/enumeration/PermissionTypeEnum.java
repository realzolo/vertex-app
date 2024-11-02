/*
 * Copyright (c) 2022-present Charles7c Authors. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.onezol.vertex.framework.security.api.enumeration;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.onezol.vertex.framework.common.constant.enumeration.Enumeration;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 菜单类型枚举
 *
 * @author Charles7c
 * @since 2023/2/15 20:12
 */
@Getter
@RequiredArgsConstructor
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
