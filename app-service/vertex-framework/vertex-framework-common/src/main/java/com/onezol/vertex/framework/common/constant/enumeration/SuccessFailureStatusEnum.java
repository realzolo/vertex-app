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
import com.onezol.vertex.framework.common.constant.ColorConstants;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Schema(name = "成功/失败状态")
@Getter
@RequiredArgsConstructor
public enum SuccessFailureStatusEnum implements Enumeration<String> {

    /**
     * 成功
     */
    SUCCESS("成功", "SUCCESS", ColorConstants.COLOR_SUCCESS),

    /**
     * 失败
     */
    FAILURE("失败", "FAILURE", ColorConstants.COLOR_ERROR);


    private final String name;

    @EnumValue
    private final String value;

    private final String color;

}
