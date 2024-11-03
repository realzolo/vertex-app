package com.onezol.vertex.framework.common.annotation;

import java.lang.annotation.*;

/**
 * 字典注解
 */
@Documented
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Dictionary {

    /**
     * 字典注解: 枚举值
     */
    String value();

}
