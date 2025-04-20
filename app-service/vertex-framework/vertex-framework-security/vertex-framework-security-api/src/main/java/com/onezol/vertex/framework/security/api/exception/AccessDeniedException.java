package com.onezol.vertex.framework.security.api.exception;

import com.onezol.vertex.framework.common.constant.enumeration.ServiceStatus;
import com.onezol.vertex.framework.common.exception.ServiceException;

/**
 * AccessDeniedException 无权限异常
 */
public class AccessDeniedException extends ServiceException {

    public AccessDeniedException() {
        super(ServiceStatus.FORBIDDEN);
    }

    public AccessDeniedException(String message) {
        super(ServiceStatus.FORBIDDEN, message);
    }

}
