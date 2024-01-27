package com.onezol.vertex.framework.common.helper;

import com.onezol.vertex.framework.common.constant.enums.BizHttpStatus;
import com.onezol.vertex.framework.common.exception.BusinessException;
import com.onezol.vertex.framework.common.model.pojo.ResponseModel;
import lombok.Data;

import java.io.Serializable;

/**
 * 统一返回结果类
 */
@Data
public final class ResponseHelper {

    public static <T> ResponseModel<T> buildSuccessfulResponse() {
        return new ResponseModel<>(BizHttpStatus.SUCCESS.getCode(), true, BizHttpStatus.SUCCESS.getValue(), null);
    }

    public static <T> ResponseModel<T> buildSuccessfulResponse(T data) {
        return new ResponseModel<>(BizHttpStatus.SUCCESS.getCode(), true, BizHttpStatus.SUCCESS.getValue(), data);
    }

    public static <T> ResponseModel<T> buildSuccessfulResponse(String message, T data) {
        return new ResponseModel<>(BizHttpStatus.SUCCESS.getCode(), true, message, data);
    }

    public static <T> ResponseModel<T> buildFailedResponse() {
        return new ResponseModel<>(BizHttpStatus.INTERNAL_SERVER_ERROR.getCode(), false, BizHttpStatus.INTERNAL_SERVER_ERROR.getValue(), null);
    }

    public static <T> ResponseModel<T> buildFailedResponse(String message) {
        return new ResponseModel<>(BizHttpStatus.INTERNAL_SERVER_ERROR.getCode(), false, message, null);
    }

    public static <T> ResponseModel<T> buildFailedResponse(int code, String message) {
        return new ResponseModel<>(code, false, message, null);
    }

    public static <T> ResponseModel<T> buildFailedResponse(BizHttpStatus bizHttpStatus) {
        return new ResponseModel<>(bizHttpStatus.getCode(), false, bizHttpStatus.getValue(), null);
    }

    public static <T> ResponseModel<T> buildFailedResponse(BizHttpStatus bizHttpStatus, String message) {
        return new ResponseModel<>(bizHttpStatus.getCode(), false, message, null);
    }

    public static <T> ResponseModel<T> buildFailedResponse(BusinessException exception) {
        return new ResponseModel<>(exception.getCode(), false, exception.getMessage(), null);
    }

}
