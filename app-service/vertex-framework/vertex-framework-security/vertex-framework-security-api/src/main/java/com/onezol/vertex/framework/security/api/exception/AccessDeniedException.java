package com.onezol.vertex.framework.security.api.exception;

import com.onezol.vertex.framework.common.constant.enums.BizHttpStatus;
import com.onezol.vertex.framework.common.exception.BusinessException;

/**
 * AccessDeniedException 无权限异常
 */
public class AccessDeniedException extends BusinessException {

    public AccessDeniedException() {
        super(BizHttpStatus.FORBIDDEN, BizHttpStatus.FORBIDDEN.getValue());
    }

    public AccessDeniedException(String message) {
        super(BizHttpStatus.FORBIDDEN, message);
    }

}
