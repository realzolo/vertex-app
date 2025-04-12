package com.onezol.vertex.framework.support.support;

import com.onezol.vertex.framework.common.constant.enumeration.ServiceStatusEnum;
import com.onezol.vertex.framework.common.exception.ServiceException;
import com.onezol.vertex.framework.common.model.GenericResponse;

/**
 * 响应构建帮助类
 */
public final class ResponseHelper {

    private ResponseHelper() {
        throw new IllegalStateException("Utility class cannot be instantiated");
    }

    public static <T> GenericResponse<T> buildSuccessfulResponse() {
        return new GenericResponse<>(ServiceStatusEnum.SUCCESS.getValue(), true, ServiceStatusEnum.SUCCESS.getDescription(), null);
    }

    public static <T> GenericResponse<T> buildSuccessfulResponse(T data) {
        return new GenericResponse<>(ServiceStatusEnum.SUCCESS.getValue(), true, ServiceStatusEnum.SUCCESS.getDescription(), data);
    }

    public static <T> GenericResponse<T> buildSuccessfulResponse(String message, T data) {
        return new GenericResponse<>(ServiceStatusEnum.SUCCESS.getValue(), true, message, data);
    }

    public static <T> GenericResponse<T> buildFailedResponse() {
        return new GenericResponse<>(ServiceStatusEnum.BAD_REQUEST.getValue(), false, ServiceStatusEnum.BAD_REQUEST.getDescription(), null);
    }

    public static <T> GenericResponse<T> buildFailedResponse(String message) {
        return new GenericResponse<>(ServiceStatusEnum.BAD_REQUEST.getValue(), false, message, null);
    }

    public static <T> GenericResponse<T> buildFailedResponse(int code, String message) {
        return new GenericResponse<>(code, false, message, null);
    }

    public static <T> GenericResponse<T> buildFailedResponse(ServiceStatusEnum ServiceStatusEnum) {
        return new GenericResponse<>(ServiceStatusEnum.getValue(), false, ServiceStatusEnum.getDescription(), null);
    }

    public static <T> GenericResponse<T> buildFailedResponse(ServiceStatusEnum ServiceStatusEnum, String message) {
        return new GenericResponse<>(ServiceStatusEnum.getValue(), false, message, null);
    }

    public static <T> GenericResponse<T> buildFailedResponse(ServiceException exception) {
        return new GenericResponse<>(exception.getCode(), false, exception.getMessage(), null);
    }

}
