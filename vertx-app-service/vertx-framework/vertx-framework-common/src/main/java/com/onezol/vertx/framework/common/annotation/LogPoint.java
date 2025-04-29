package com.onezol.vertx.framework.common.annotation;

import java.lang.annotation.*;

/**
 * 日志切点注解
 */
@Documented
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface LogPoint {

    /**
     * 是否跳过当前方法/类的日志记录<br/>
     * 标注在类上时，表示不对当前类的所有方法进行日志记录<br/>
     * 标注在方法上时，表示不对当前方法进行日志记录
     */
    boolean skip() default false;

}
