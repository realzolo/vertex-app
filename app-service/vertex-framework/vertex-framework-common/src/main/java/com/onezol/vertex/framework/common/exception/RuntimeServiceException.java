package com.onezol.vertex.framework.common.exception;


import com.onezol.vertex.framework.common.constant.enumeration.ServiceStatusEnum;

/**
 * 运行时服务异常（此异常会抛给前端）
 */
public class RuntimeServiceException extends ServiceException {

    public RuntimeServiceException(String message) {
        super(message);
    }

    public RuntimeServiceException(ServiceStatusEnum serviceStatus) {
        super(serviceStatus);
    }

    public RuntimeServiceException(ServiceStatusEnum serviceStatus, String message) {
        super(serviceStatus, message);
    }

}
