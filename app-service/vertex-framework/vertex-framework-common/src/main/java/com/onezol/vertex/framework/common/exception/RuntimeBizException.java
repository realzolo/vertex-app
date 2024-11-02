package com.onezol.vertex.framework.common.exception;


import com.onezol.vertex.framework.common.constant.enumeration.BizHttpStatusEnum;

/**
 * 运行时业务异常（此异常会抛给前端）
 */
public class RuntimeBizException extends BusinessException {

    public RuntimeBizException(String message) {
        super(message);
    }

    public RuntimeBizException(BizHttpStatusEnum bizHttpStatus) {
        super(bizHttpStatus);
    }

    public RuntimeBizException(BizHttpStatusEnum bizHttpStatus, String message) {
        super(bizHttpStatus, message);
    }

}
