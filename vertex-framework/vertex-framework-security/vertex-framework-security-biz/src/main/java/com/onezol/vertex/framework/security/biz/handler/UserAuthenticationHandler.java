package com.onezol.vertex.framework.security.biz.handler;

import com.onezol.vertex.framework.common.util.ServletUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;


/**
 * 安全认证入口点处理器: 用于处理认证过程中的异常
 */
@Component
public class UserAuthenticationHandler implements AuthenticationEntryPoint {

    /**
     * 当用户访问需要进行身份验证的资源但未通过身份验证时，将触发身份验证异常
     *
     * @param request       请求
     * @param response      响应
     * @param authException 认证异常
     */
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) {
        ServletUtils.writeJSON(response, new com.onezol.vertex.framework.security.api.exception.AuthenticationException("当前请求需要用户认证，请登录后重试"));
    }

}
