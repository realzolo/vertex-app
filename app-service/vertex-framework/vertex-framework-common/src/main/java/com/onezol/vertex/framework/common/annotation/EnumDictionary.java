package com.onezol.vertex.framework.common.annotation;

import java.lang.annotation.*;

@Documented
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface EnumDictionary {

    String name();

    String value();

}
