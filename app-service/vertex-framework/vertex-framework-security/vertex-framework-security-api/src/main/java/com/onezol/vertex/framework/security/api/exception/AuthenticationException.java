package com.onezol.vertex.framework.security.api.exception;

import com.onezol.vertex.framework.common.constant.enumeration.ServiceStatus;
import com.onezol.vertex.framework.common.exception.ServiceException;

/**
 * 认证异常
 */
public class AuthenticationException extends ServiceException {

    public AuthenticationException() {
        super(ServiceStatus.UNAUTHORIZED);
    }

    public AuthenticationException(String message) {
        super(ServiceStatus.UNAUTHORIZED, message);
    }

}