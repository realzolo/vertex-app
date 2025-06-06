package com.onezol.vertx.framework.component.websocket.annotation;

import java.lang.annotation.*;

@Documented
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface WebSocketMessageListener {

    String group();

}
