package com.onezol.vertex.framework.component.monitor.controller;

import com.alibaba.fastjson2.JSONObject;
import com.onezol.vertex.framework.common.helper.ResponseHelper;
import com.onezol.vertex.framework.common.model.pojo.ResponseModel;
import com.onezol.vertex.framework.common.util.StringUtils;
import com.onezol.vertex.framework.security.api.annotation.RestrictAccess;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@Tag(name = "应用监控")
@Slf4j
@RestController
@RequestMapping("/monitor/application")
public class ApplicationMonitorController {

    private final RestTemplate restTemplate;

    @Value("${server.port:8080}")
    private String port;

    @Value("${server.servlet.context-path:}")
    private String contextPath;

    public ApplicationMonitorController(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Operation(summary = "获取应用信息", description = "获取应用监控信息，数据来源于Spring Boot Actuator")
    @RestrictAccess
    @GetMapping("/**")
    public ResponseModel<JSONObject> info(HttpServletRequest request, @RequestParam(value = "tag", required = false) String tag) {
        String requestURI = request.getRequestURI();
        String key = requestURI.replace(contextPath + "/monitor/application/", "");
        String url = String.format("http://127.0.0.1:%s%s/actuator/%s", port, contextPath, key);
        if (StringUtils.isNotBlank(tag)) {
            url += "?tag=" + tag;
        }
        JSONObject result = restTemplate.getForObject(url, JSONObject.class);
        return ResponseHelper.buildSuccessfulResponse(result);
    }

}
