package com.onezol.vertx.framework.common.exception;

import com.onezol.vertx.framework.common.constant.enumeration.ServiceStatus;

/**
 * 数据状态异常: 数据处于不正确的状态
 */
public class InvalidDataStateException extends ServiceException {

    public InvalidDataStateException(String message) {
        super(ServiceStatus.INTERNAL_SERVER_ERROR, message);
    }

}
