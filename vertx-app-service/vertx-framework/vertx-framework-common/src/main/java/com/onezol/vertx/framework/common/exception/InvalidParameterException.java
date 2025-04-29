package com.onezol.vertx.framework.common.exception;

import com.onezol.vertx.framework.common.constant.enumeration.ServiceStatus;

/**
 * 请求参数异常: 通用参数不合法（如类型错误、格式错误、必填参数缺失等）。
 */
public class InvalidParameterException extends ServiceException  {

    public InvalidParameterException(String message) {
        super(ServiceStatus.BAD_REQUEST, message);
    }

}
