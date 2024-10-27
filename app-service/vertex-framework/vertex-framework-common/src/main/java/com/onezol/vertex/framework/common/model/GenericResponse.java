package com.onezol.vertex.framework.common.model;

import lombok.Data;

import java.io.Serializable;
import java.util.UUID;

/**
 * 统一响应模型
 */
@Data
public class GenericResponse<T> implements Serializable {

    private int code;

    private boolean success;

    private String message;

    private T data;

    private String traceId;

    private String timestamp;

    public GenericResponse(int code, boolean success, String message, T data) {
        this.code = code;
        this.success = success;
        this.message = message;
        this.data = data;
        this.traceId = UUID.randomUUID().toString().replace("-", "");
        this.timestamp = String.valueOf(System.currentTimeMillis());
    }
}
