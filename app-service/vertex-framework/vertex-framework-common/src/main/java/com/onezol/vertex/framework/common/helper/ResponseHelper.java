package com.onezol.vertex.framework.common.helper;

import com.onezol.vertex.framework.common.constant.enumeration.BizHttpStatusEnum;
import com.onezol.vertex.framework.common.exception.BusinessException;
import com.onezol.vertex.framework.common.model.GenericResponse;
import lombok.Data;

/**
 * 统一返回结果类
 */
@Data
public final class ResponseHelper {

    public static <T> GenericResponse<T> buildSuccessfulResponse() {
        return new GenericResponse<>(BizHttpStatusEnum.SUCCESS.getValue(), true, BizHttpStatusEnum.SUCCESS.getDescription(), null);
    }

    public static <T> GenericResponse<T> buildSuccessfulResponse(T data) {
        return new GenericResponse<>(BizHttpStatusEnum.SUCCESS.getValue(), true, BizHttpStatusEnum.SUCCESS.getDescription(), data);
    }

    public static <T> GenericResponse<T> buildSuccessfulResponse(String message, T data) {
        return new GenericResponse<>(BizHttpStatusEnum.SUCCESS.getValue(), true, message, data);
    }

    public static <T> GenericResponse<T> buildFailedResponse() {
        return new GenericResponse<>(BizHttpStatusEnum.INTERNAL_SERVER_ERROR.getValue(), false, BizHttpStatusEnum.INTERNAL_SERVER_ERROR.getDescription(), null);
    }

    public static <T> GenericResponse<T> buildFailedResponse(String message) {
        return new GenericResponse<>(BizHttpStatusEnum.INTERNAL_SERVER_ERROR.getValue(), false, message, null);
    }

    public static <T> GenericResponse<T> buildFailedResponse(int code, String message) {
        return new GenericResponse<>(code, false, message, null);
    }

    public static <T> GenericResponse<T> buildFailedResponse(BizHttpStatusEnum BizHttpStatusEnum) {
        return new GenericResponse<>(BizHttpStatusEnum.getValue(), false, BizHttpStatusEnum.getDescription(), null);
    }

    public static <T> GenericResponse<T> buildFailedResponse(BizHttpStatusEnum BizHttpStatusEnum, String message) {
        return new GenericResponse<>(BizHttpStatusEnum.getValue(), false, message, null);
    }

    public static <T> GenericResponse<T> buildFailedResponse(BusinessException exception) {
        return new GenericResponse<>(exception.getCode(), false, exception.getMessage(), null);
    }

}
