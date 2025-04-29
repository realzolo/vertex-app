package com.onezol.vertx.framework.common.exception;

import com.onezol.vertx.framework.common.constant.enumeration.ServiceStatus;

/**
 * 远程调用异常: 远程服务调用失败
 */
public class RemoteCallException extends ServiceException {

    public RemoteCallException(String message) {
        super(ServiceStatus.INTERNAL_SERVER_ERROR, message);
    }
}
