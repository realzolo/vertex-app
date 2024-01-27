package com.onezol.vertex.framework.common.util;

import com.onezol.vertex.framework.common.constant.enums.BizHttpStatus;
import com.onezol.vertex.framework.common.model.pojo.ResponseModel;
import lombok.Data;

import java.io.Serializable;

/**
 * 统一返回结果类
 */
@Data
public class ResponseHelper implements Serializable {

    public static <T> ResponseModel<T> buildSuccessfulResponse() {
        return new ResponseModel<>(BizHttpStatus.SUCCESS.getCode(), true, BizHttpStatus.SUCCESS.getValue(), null);
    }

    public static <T> ResponseModel<T> buildSuccessfulResponse(T data) {
        return new ResponseModel<>(BizHttpStatus.SUCCESS.getCode(), true, BizHttpStatus.SUCCESS.getValue(), data);
    }

    public static <T> ResponseModel<T> buildSuccessfulResponse(String message, T data) {
        return new ResponseModel<>(BizHttpStatus.SUCCESS.getCode(), true, message, data);
    }

    public static <T> ResponseModel<T> buildFailureResponse() {
        return new ResponseModel<>(BizHttpStatus.INTERNAL_SERVER_ERROR.getCode(), false, BizHttpStatus.INTERNAL_SERVER_ERROR.getValue(), null);
    }

    public static <T> ResponseModel<T> buildFailureResponse(String message) {
        return new ResponseModel<>(BizHttpStatus.INTERNAL_SERVER_ERROR.getCode(), false, message, null);
    }

    public static <T> ResponseModel<T> buildFailureResponse(int code, String message) {
        return new ResponseModel<>(code, false, message, null);
    }

    public static <T> ResponseModel<T> buildFailureResponse(BizHttpStatus bizHttpStatus) {
        return new ResponseModel<>(bizHttpStatus.getCode(), false, bizHttpStatus.getValue(), null);
    }
}
