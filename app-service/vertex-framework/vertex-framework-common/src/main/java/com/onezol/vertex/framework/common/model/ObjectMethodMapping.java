package com.onezol.vertex.framework.common.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.lang.reflect.Method;

@Data
@AllArgsConstructor
public class ObjectMethodMapping {

    private Object object;

    private Method method;

}
