package com.onezol.vertex.framework.security.biz.handler;

import com.onezol.vertex.framework.common.model.GenericResponse;
import com.onezol.vertex.framework.common.helper.ResponseHelper;
import com.onezol.vertex.framework.common.util.ServletUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

/**
 * 拒绝访问处理类(权限不足)
 */
@Component
public class UserAccessDeniedHandler implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) {
        GenericResponse<Object> genericResponse = ResponseHelper.buildFailedResponse(new com.onezol.vertex.framework.security.api.exception.AccessDeniedException("访问受限, 您的权限不足"));
        ServletUtils.writeJSON(response, genericResponse);
    }

}
