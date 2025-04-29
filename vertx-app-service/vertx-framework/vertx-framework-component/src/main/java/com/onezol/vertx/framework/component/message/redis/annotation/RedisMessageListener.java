package com.onezol.vertx.framework.component.message.redis.annotation;

import java.lang.annotation.*;

@Documented
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface RedisMessageListener {

    String stream();

    String group();

}
