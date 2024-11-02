package com.onezol.vertex.framework.security.api.exception;

import com.onezol.vertex.framework.common.constant.enumeration.BizHttpStatusEnum;
import com.onezol.vertex.framework.common.exception.BusinessException;

/**
 * AccessDeniedException 无权限异常
 */
public class AccessDeniedException extends BusinessException {

    public AccessDeniedException() {
        super(BizHttpStatusEnum.FORBIDDEN);
    }

    public AccessDeniedException(String message) {
        super(BizHttpStatusEnum.FORBIDDEN, message);
    }

}
