package com.onezol.vertex.framework.security.biz.handler;

import com.onezol.vertex.framework.common.model.pojo.ResponseModel;
import com.onezol.vertex.framework.common.util.ResponseHelper;
import com.onezol.vertex.framework.common.util.ServletUtils;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * 拒绝访问处理类(权限不足)
 */
@Component
public class UserAccessDeniedHandler implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) {
        ResponseModel<Object> responseModel = ResponseHelper.buildFailureResponse(new com.onezol.vertex.framework.security.api.exception.AccessDeniedException("访问受限, 您的权限不足"));
        ServletUtils.writeJSON(response, responseModel);
    }

}
