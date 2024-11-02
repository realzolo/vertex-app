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

package com.onezol.vertex.framework.common.constant.enumeration;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 性别枚举
 */
@Getter
@RequiredArgsConstructor
public enum GenderEnum implements Enumeration<Integer> {

    /**
     * 未知
     */
    UNKNOWN("未知", 0),

    /**
     * 男
     */
    MALE("男", 1),

    /**
     * 女
     */
    FEMALE("女", 2);


    private final String name;

    @EnumValue
    private final Integer value;
}
