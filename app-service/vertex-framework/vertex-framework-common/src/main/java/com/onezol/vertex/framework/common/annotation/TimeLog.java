package com.onezol.vertex.framework.common.annotation;

import java.lang.annotation.*;

/**
 * 时间日志注解: 用于输出方法执行时间
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface TimeLog {
}
