package com.onezol.vertex.framework.common.annotation;

import java.lang.annotation.*;

/**
 * 字典翻译启用注解
 */
@Documented
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface UseDictionary {
}
