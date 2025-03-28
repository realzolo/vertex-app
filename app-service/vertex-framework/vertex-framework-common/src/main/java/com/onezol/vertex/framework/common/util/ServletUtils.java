package com.onezol.vertex.framework.common.util;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import eu.bitwalker.useragentutils.UserAgent;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.util.StreamUtils;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public final class ServletUtils {

    /**
     * 获取当前request
     *
     * @return request
     */
    public static HttpServletRequest getRequest() {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        if (requestAttributes instanceof ServletRequestAttributes servletRequestAttributes) {
            return servletRequestAttributes.getRequest();
        } else {
            throw new RuntimeException("当前线程中不存在 Request 对象");
        }
    }

    /**
     * 获取Request Header
     *
     * @param name 属性名
     * @return 属性值
     */
    public static String getHeader(String name) {
        return getRequest().getHeader(name);
    }


    /**
     * 获取Request Session
     *
     * @return session
     */
    public static HttpSession getSession() {
        return getRequest().getSession();
    }

    /**
     * 获取Request User-Agent
     *
     * @return ua
     */
    public static UserAgent getUserAgent() {
        HttpServletRequest request = getRequest();
        String ua = request.getHeader("User-Agent");
        return UserAgent.parseUserAgentString(ua);
    }

    /**
     * 获取Request Client IP
     *
     * @return ip
     */
    public static String getClientIP() {
        HttpServletRequest request = getRequest();
        return NetworkUtils.getIpAddr(request);
    }

    /**
     * 获取Request Param
     */
    public static Map<String, String> getRequestParam(HttpServletRequest request) {
        Map<String, String> paramMap = new HashMap<>();
        Map<String, String[]> parameterMap = request.getParameterMap();
        for (Map.Entry<String, String[]> entry : parameterMap.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue()[0];  // 只获取第一个值
            paramMap.put(key, value);
        }
        return paramMap;
    }

    /**
     * 获取Request Body
     */
    public static Map<String, Object> getRequestBody(HttpServletRequest request) throws IOException {
        String requestBody = StreamUtils.copyToString(request.getInputStream(), StandardCharsets.UTF_8);
        if (requestBody.isBlank()) {
            return Collections.emptyMap();
        }
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(requestBody, new TypeReference<>() {
        });
    }

    /**
     * 响应数据
     *
     * @param response response
     */
    public static void write(HttpServletResponse response, String mediaType, Object data) {
        String content = JsonUtils.toJsonString(data);
        response.setStatus(HttpServletResponse.SC_OK);
        response.setContentType(mediaType);
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        try {
            PrintWriter writer = response.getWriter();
            writer.write(content);
            writer.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 响应JSON字符串
     *
     * @param response response
     */
    public static void writeJSON(HttpServletResponse response, Object data) {
        write(response, MediaType.APPLICATION_JSON_VALUE, data);
    }

    /**
     * 响应附件
     *
     * @param response 响应
     * @param filename 文件名
     * @param content  附件内容
     */
    public static void writeAttachment(HttpServletResponse response, String filename, byte[] content) throws IOException {
        response.setStatus(HttpServletResponse.SC_OK);
        response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(filename, StandardCharsets.UTF_8));
        response.setContentType(MediaType.APPLICATION_OCTET_STREAM_VALUE);
        response.getOutputStream().write(content);
    }
}
