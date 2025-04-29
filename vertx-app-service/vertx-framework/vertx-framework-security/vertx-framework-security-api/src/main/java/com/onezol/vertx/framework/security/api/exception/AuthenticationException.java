package com.onezol.vertx.framework.security.api.exception;

import com.onezol.vertx.framework.common.constant.enumeration.ServiceStatus;
import com.onezol.vertx.framework.common.exception.ServiceException;

/**
 * 认证异常
 * <br/>
 *      > 用户未提供身份凭证（如未登录）<br/>
 *      > 提供的凭证无效（如错误的用户名/密码、过期的 Token）<br/>
 *      > 身份验证失败（如 JWT 签名无效）<br/>
 */
public class AuthenticationException extends ServiceException {

    public AuthenticationException() {
        super(ServiceStatus.UNAUTHORIZED);
    }

    public AuthenticationException(String message) {
        super(ServiceStatus.UNAUTHORIZED, message);
    }

}