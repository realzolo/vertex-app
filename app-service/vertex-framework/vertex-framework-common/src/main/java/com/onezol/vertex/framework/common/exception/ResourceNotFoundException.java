package com.onezol.vertex.framework.common.exception;

import com.onezol.vertex.framework.common.constant.enumeration.ServiceStatus;

/**
 * 资源不存在异常
 */
public class ResourceNotFoundException extends ServiceException {

    public ResourceNotFoundException(String message) {
        super(ServiceStatus.NOT_FOUND, message);
    }

}
