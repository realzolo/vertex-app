package com.onezol.vertex.framework.common.helper;

import com.onezol.vertex.framework.common.constant.enums.BizHttpStatus;
import com.onezol.vertex.framework.common.exception.BusinessException;
import com.onezol.vertex.framework.common.model.GenericResponse;
import lombok.Data;

/**
 * 统一返回结果类
 */
@Data
public final class ResponseHelper {

    public static <T> GenericResponse<T> buildSuccessfulResponse() {
        return new GenericResponse<>(BizHttpStatus.SUCCESS.getCode(), true, BizHttpStatus.SUCCESS.getValue(), null);
    }

    public static <T> GenericResponse<T> buildSuccessfulResponse(T data) {
        return new GenericResponse<>(BizHttpStatus.SUCCESS.getCode(), true, BizHttpStatus.SUCCESS.getValue(), data);
    }

    public static <T> GenericResponse<T> buildSuccessfulResponse(String message, T data) {
        return new GenericResponse<>(BizHttpStatus.SUCCESS.getCode(), true, message, data);
    }

    public static <T> GenericResponse<T> buildFailedResponse() {
        return new GenericResponse<>(BizHttpStatus.INTERNAL_SERVER_ERROR.getCode(), false, BizHttpStatus.INTERNAL_SERVER_ERROR.getValue(), null);
    }

    public static <T> GenericResponse<T> buildFailedResponse(String message) {
        return new GenericResponse<>(BizHttpStatus.INTERNAL_SERVER_ERROR.getCode(), false, message, null);
    }

    public static <T> GenericResponse<T> buildFailedResponse(int code, String message) {
        return new GenericResponse<>(code, false, message, null);
    }

    public static <T> GenericResponse<T> buildFailedResponse(BizHttpStatus bizHttpStatus) {
        return new GenericResponse<>(bizHttpStatus.getCode(), false, bizHttpStatus.getValue(), null);
    }

    public static <T> GenericResponse<T> buildFailedResponse(BizHttpStatus bizHttpStatus, String message) {
        return new GenericResponse<>(bizHttpStatus.getCode(), false, message, null);
    }

    public static <T> GenericResponse<T> buildFailedResponse(BusinessException exception) {
        return new GenericResponse<>(exception.getCode(), false, exception.getMessage(), null);
    }

}
