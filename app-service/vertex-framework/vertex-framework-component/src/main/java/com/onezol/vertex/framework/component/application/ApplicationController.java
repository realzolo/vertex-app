package com.onezol.vertex.framework.component.application;

import com.onezol.vertex.framework.common.helper.ResponseHelper;
import com.onezol.vertex.framework.common.model.pojo.ResponseModel;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringBootVersion;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "应用信息")
@RestController
@RequestMapping("/application")
public class ApplicationController {

    @Value("${java.version}")
    private String javaVersion;

    @Value("${spring.application.name}")
    private String applicationName;

    @Value("${application.version:1.0.0}")
    private String applicationVersion;

    @Operation(summary = "应用信息", description = "应用信息")
    @GetMapping
    public ResponseModel<String> information() {
        String appInfo = """
                <pre style="font-weight: bold; color: #333; font-size: 14px; line-height: 24px;">
                    Application: %s
                                
                    Java Version: %s
                                
                    Spring Boot Version: %s
                                
                    CopyRight: xm.l 2024
                </pre>            \s
                """.formatted(applicationName, javaVersion, SpringBootVersion.getVersion());
        return ResponseHelper.buildSuccessfulResponse(appInfo);
    }

    @Operation(summary = "获取版本号", description = "获取应用版本号")
    @GetMapping("/version")
    public ResponseModel<String> version() {
        String version = String.format("V%s", applicationVersion);
        return ResponseHelper.buildSuccessfulResponse(version);
    }

}
