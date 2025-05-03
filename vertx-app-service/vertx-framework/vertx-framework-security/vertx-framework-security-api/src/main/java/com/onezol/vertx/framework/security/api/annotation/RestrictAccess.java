package com.onezol.vertx.framework.security.api.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 访问控制注解。<br/>
 * 用于标注需要进行访问控制的接口，如果接口未标注该注解，则不进行访问控制检查。<br/>
 * 用于标注类时，表示该类下的所有接口都需要进行访问控制检查。<br/>
 * 用于标注方法时，表示该方法需要进行访问控制检查。
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Deprecated
public @interface RestrictAccess {
}
