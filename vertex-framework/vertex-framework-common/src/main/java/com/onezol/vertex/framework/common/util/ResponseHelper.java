package com.onezol.vertex.framework.common.util;

import com.onezol.vertex.framework.common.constant.enums.HttpStatus;
import com.onezol.vertex.framework.common.model.pojo.ResponseModel;
import lombok.Data;

import java.io.Serializable;

/**
 * 统一返回结果类
 */
@Data
public class ResponseHelper implements Serializable {

    public static <T> ResponseModel<T> buildSuccessfulResponse() {
        return new ResponseModel<>(HttpStatus.SUCCESS.getCode(), true, HttpStatus.SUCCESS.getValue(), null);
    }

    public static <T> ResponseModel<T> buildSuccessfulResponse(T data) {
        return new ResponseModel<>(HttpStatus.SUCCESS.getCode(), true, HttpStatus.SUCCESS.getValue(), data);
    }

    public static <T> ResponseModel<T> buildSuccessfulResponse(String message, T data) {
        return new ResponseModel<>(HttpStatus.SUCCESS.getCode(), true, message, data);
    }

    public static <T> ResponseModel<T> buildFailureResponse() {
        return new ResponseModel<>(HttpStatus.FAILURE.getCode(), false, HttpStatus.FAILURE.getValue(), null);
    }

    public static <T> ResponseModel<T> buildFailureResponse(String message) {
        return new ResponseModel<>(HttpStatus.FAILURE.getCode(), false, message, null);
    }

    public static <T> ResponseModel<T> buildFailureResponse(int code, String message) {
        return new ResponseModel<>(code, false, message, null);
    }

    public static <T> ResponseModel<T> buildFailureResponse(HttpStatus httpStatus) {
        return new ResponseModel<>(httpStatus.getCode(), false, httpStatus.getValue(), null);
    }
}
