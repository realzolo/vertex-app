package com.onezol.vertex.framework.common.constant.enumeration;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.onezol.vertex.framework.common.annotation.EnumDictionary;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Schema(name = "账号状态")
@Getter
@AllArgsConstructor
@EnumDictionary(name = "账号状态", value = "account_status")
public enum AccountStatusEnum implements Enumeration<Integer> {

    ACTIVE("正常", 0),

    LOCKED("锁定", 1),

    DISABLED("禁用", 2),

    EXPIRED("过期", 3);


    private final String name;

    @EnumValue
    private final Integer value;

}
