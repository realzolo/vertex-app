package com.onezol.vertex.framework.common.constant.enumeration;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 账户状态枚举
 */
@Getter
@AllArgsConstructor
public enum AccountStatusEnum implements Enumeration<Integer> {

    ACTIVE("正常", 0),

    LOCKED("锁定", 1),

    DISABLED("禁用", 2),

    EXPIRED("过期", 3);


    private final String name;

    @EnumValue
    private final Integer value;

}
