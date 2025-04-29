package com.onezol.vertx.framework.common.exception;


import com.onezol.vertx.framework.common.constant.enumeration.ServiceStatus;

/**
 * 运行时服务异常（此异常会抛给前端）
 */
public class RuntimeServiceException extends ServiceException {

    public RuntimeServiceException(String message) {
        super(message);
    }

    public RuntimeServiceException(ServiceStatus serviceStatus) {
        super(serviceStatus);
    }

    public RuntimeServiceException(ServiceStatus serviceStatus, String message) {
        super(serviceStatus, message);
    }

}
