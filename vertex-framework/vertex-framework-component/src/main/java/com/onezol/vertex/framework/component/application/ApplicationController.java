package com.onezol.vertex.framework.component.application;

import com.onezol.vertex.framework.security.api.annotation.RestrictAccess;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringBootVersion;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Application")
@RestController
@RequestMapping("/application")
public class ApplicationController {

    @Value("${java.version}")
    private String javaVersion;

    @Value("${spring.application.name}")
    private String applicationName;

    @Operation(summary = "应用信息", description = "应用信息")
    @GetMapping
    public String information() {
        return """
                <pre style="font-weight: bold; color: #333; font-size: 14px; line-height: 24px;">
                    Application: %s
                                
                    Java Version: %s
                                
                    Spring Boot Version: %s
                                
                    CopyRight: xm.l 2024
                </pre>            \s
                """.formatted(applicationName, javaVersion, SpringBootVersion.getVersion());
    }

}
