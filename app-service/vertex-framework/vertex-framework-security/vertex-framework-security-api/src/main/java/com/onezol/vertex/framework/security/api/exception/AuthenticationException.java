package com.onezol.vertex.framework.security.api.exception;


import com.onezol.vertex.framework.common.constant.enumeration.BizHttpStatusEnum;
import com.onezol.vertex.framework.common.exception.BusinessException;

/**
 * 认证异常
 */
public class AuthenticationException extends BusinessException {

    public AuthenticationException() {
        super(BizHttpStatusEnum.UNAUTHORIZED);
    }

    public AuthenticationException(String message) {
        super(BizHttpStatusEnum.UNAUTHORIZED, message);
    }

}