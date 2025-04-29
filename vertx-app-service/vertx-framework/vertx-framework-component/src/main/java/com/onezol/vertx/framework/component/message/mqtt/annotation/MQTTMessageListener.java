package com.onezol.vertx.framework.component.message.mqtt.annotation;

import java.lang.annotation.*;

@Documented
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface MQTTMessageListener {

    String topic();

}
