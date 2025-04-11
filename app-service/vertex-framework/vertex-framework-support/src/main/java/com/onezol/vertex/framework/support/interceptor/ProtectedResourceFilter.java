package com.onezol.vertex.framework.support.interceptor;

import com.onezol.vertex.framework.common.constant.enumeration.ServiceStatusEnum;
import com.onezol.vertex.framework.support.support.ResponseHelper;
import com.onezol.vertex.framework.common.model.GenericResponse;
import com.onezol.vertex.framework.common.util.ServletUtils;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.io.IOException;

import static com.onezol.vertex.framework.support.interceptor.RequestInterceptionOrder.PROTECTED_RESOURCE_FILTER_ORDER;

/**
 * 受保护资源拦截器<br>
 * 此处拦截系统中禁止对外暴露的资源(一般拦截第三方jar包提供的接口)
 */
@Component
@Order(PROTECTED_RESOURCE_FILTER_ORDER)
public class ProtectedResourceFilter implements Filter {

    @Value("${server.servlet.context-path:}")
    private String contextPath;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        String requestUri = httpServletRequest.getRequestURI();
        String remoteHost = httpServletRequest.getRemoteHost();

        // 拦截actuator接口
        if (requestUri.startsWith(contextPath + "/actuator") && !remoteHost.equals("127.0.0.1")) {
            GenericResponse<Object> genericResponse = ResponseHelper.buildFailedResponse(ServiceStatusEnum.FORBIDDEN, "受保护资源, 禁止使用当前接口访问");
            ServletUtils.writeJSON((HttpServletResponse) response, genericResponse);
            return;
        }

        chain.doFilter(request, response);
    }

}
