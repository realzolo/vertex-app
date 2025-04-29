package com.onezol.vertx.framework.common.annotation;

import org.springframework.stereotype.Component;

import java.lang.annotation.*;

@Documented
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Component
public @interface MessageListener {
}
