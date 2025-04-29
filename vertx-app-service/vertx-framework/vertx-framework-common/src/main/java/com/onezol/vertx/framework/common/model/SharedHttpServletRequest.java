package com.onezol.vertx.framework.common.model;

import com.onezol.vertx.framework.common.util.NetworkUtils;
import com.onezol.vertx.framework.common.util.ServletUtils;
import eu.bitwalker.useragentutils.UserAgent;
import jakarta.servlet.http.HttpServletRequest;
import lombok.Getter;

import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

/**
 * 共享HttpServletRequest，一般用于线程共享Request
 */
@Getter
public class SharedHttpServletRequest {

    private final String requestURI;

    private final String method;

    private final Map<String, String[]> parameters;

    private final Map<String, Object> body;

    private final Map<String, String> headers;

    private final Map<String, Object> sessionAttributes;

    private final UserAgent userAgent;

    private final String remoteAddr;

    public SharedHttpServletRequest(HttpServletRequest request) {
        this.method = request.getMethod();
        this.requestURI = request.getRequestURI();
        this.parameters = request.getParameterMap();
        try {
            this.body = ServletUtils.getRequestBody(request);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        this.headers = this.extractHeaders(request);
        this.sessionAttributes = this.extractSessionAttributes(request);
        this.userAgent = UserAgent.parseUserAgentString(request.getHeader("User-Agent"));
        this.remoteAddr = NetworkUtils.getIpAddr(request);
    }

    public static SharedHttpServletRequest of(HttpServletRequest request) {
        return new SharedHttpServletRequest(request);
    }

    /**
     * 从HttpServletRequest中提取请求头信息
     *
     * @param request 请求
     * @return 请求头信息
     */
    private Map<String, String> extractHeaders(HttpServletRequest request) {
        Map<String, String> headers = new HashMap<>();
        Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String headerName = headerNames.nextElement();
            String headerValue = request.getHeader(headerName);
            headers.put(headerName, headerValue);
        }
        return headers;
    }

    /**
     * 从HttpServletRequest中提取会话信息
     *
     * @param request 请求
     * @return 会话信息
     */
    private Map<String, Object> extractSessionAttributes(HttpServletRequest request) {
        Map<String, Object> sessionAttributes = new HashMap<>();
        Enumeration<String> attributeNames = request.getSession().getAttributeNames();
        while (attributeNames.hasMoreElements()) {
            String attributeName = attributeNames.nextElement();
            Object attributeValue = request.getSession().getAttribute(attributeName);
            sessionAttributes.put(attributeName, attributeValue);
        }
        return sessionAttributes;
    }

}
