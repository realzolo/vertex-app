package com.onezol.vertex.framework.security.api.exception;


import com.onezol.vertex.framework.common.constant.enums.BizHttpStatus;
import com.onezol.vertex.framework.common.exception.BusinessException;

/**
 * 认证异常
 */
public class AuthenticationException extends BusinessException {

    public AuthenticationException() {
        super(BizHttpStatus.UNAUTHORIZED, BizHttpStatus.UNAUTHORIZED.getValue());
    }

    public AuthenticationException(String message) {
        super(BizHttpStatus.UNAUTHORIZED, message);
    }

}