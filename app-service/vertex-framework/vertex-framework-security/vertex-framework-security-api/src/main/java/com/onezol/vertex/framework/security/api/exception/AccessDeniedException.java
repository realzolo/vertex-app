package com.onezol.vertex.framework.security.api.exception;

import com.onezol.vertex.framework.common.constant.enumeration.ServiceStatus;
import com.onezol.vertex.framework.common.exception.ServiceException;

/**
 * AccessDeniedException 无权限异常
 * <br/>
 *      > 用户已认证，但权限不足（如普通用户尝试访问管理员接口）<br/>
 *      > 资源对当前用户明确禁止访问（如无查看权限的文件）<br/>
 */
public class AccessDeniedException extends ServiceException {

    public AccessDeniedException() {
        super(ServiceStatus.FORBIDDEN);
    }

    public AccessDeniedException(String message) {
        super(ServiceStatus.FORBIDDEN, message);
    }

}
