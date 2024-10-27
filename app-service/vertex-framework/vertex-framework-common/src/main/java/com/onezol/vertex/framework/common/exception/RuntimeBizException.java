package com.onezol.vertex.framework.common.exception;

import com.onezol.vertex.framework.common.constant.enums.BizHttpStatus;

public class RuntimeBizException extends BusinessException {

    public RuntimeBizException(String message) {
        super(message);
    }

    public RuntimeBizException(BizHttpStatus bizHttpStatus) {
        super(bizHttpStatus);
    }

    public RuntimeBizException(BizHttpStatus bizHttpStatus, String message) {
        super(bizHttpStatus, message);
    }

}
