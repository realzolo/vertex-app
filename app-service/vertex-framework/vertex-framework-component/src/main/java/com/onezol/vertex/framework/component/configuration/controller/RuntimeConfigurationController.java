package com.onezol.vertex.framework.component.configuration.controller;

import com.onezol.vertex.framework.common.constant.enumeration.ServiceStatus;
import com.onezol.vertex.framework.common.model.GenericResponse;
import com.onezol.vertex.framework.common.model.LabelValue;
import com.onezol.vertex.framework.common.model.SimpleModel;
import com.onezol.vertex.framework.component.configuration.model.RuntimeConfiguration;
import com.onezol.vertex.framework.component.configuration.service.RuntimeConfigurationService;
import com.onezol.vertex.framework.security.api.annotation.RestrictAccess;
import com.onezol.vertex.framework.security.api.context.AuthenticationContext;
import com.onezol.vertex.framework.security.api.model.dto.AuthUser;
import com.onezol.vertex.framework.support.support.ResponseHelper;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

@Tag(name = "运行时配置")
@RestController
@RequestMapping("/runtime-configuration")
public class RuntimeConfigurationController {

    private final RuntimeConfigurationService runtimeConfigurationService;

    public RuntimeConfigurationController(RuntimeConfigurationService runtimeConfigurationService) {
        this.runtimeConfigurationService = runtimeConfigurationService;
    }

    @GetMapping("/{subject}/dict")
    public GenericResponse<List<LabelValue<String, String>>> getListAsLabelValue(@PathVariable("subject") String subject) {
        List<LabelValue<String, String>> configurations;
        if (!Objects.equals("SITE", subject)) {
            AuthUser authUser = AuthenticationContext.get();
            if (Objects.isNull(authUser)) {
                return ResponseHelper.buildFailedResponse(ServiceStatus.UNAUTHORIZED);
            }
        }
        configurations = runtimeConfigurationService.listConfigurationsAsLabelValue(subject);
        return ResponseHelper.buildSuccessfulResponse(configurations);
    }

    @GetMapping
    public GenericResponse<List<SimpleModel<String>>> getListAsLabelValue1(@RequestParam("category") String subject) {
        List<SimpleModel<String>> configurations;
        if (!Objects.equals("SITE", subject)) {
            AuthUser authUser = AuthenticationContext.get();
            if (Objects.isNull(authUser)) {
                return ResponseHelper.buildFailedResponse(ServiceStatus.UNAUTHORIZED);
            }
        }
        configurations = runtimeConfigurationService.listConfigurationsAsSimpleModel(subject);
        return ResponseHelper.buildSuccessfulResponse(configurations);
    }


    @RestrictAccess
    @GetMapping("/list/{subject}")
    public GenericResponse<List<RuntimeConfiguration>> getList(@PathVariable("subject") String subject) {
        List<RuntimeConfiguration> configurations = runtimeConfigurationService.listConfigurations(subject);
        return ResponseHelper.buildSuccessfulResponse(configurations);
    }


    @RestrictAccess
    @PutMapping
    public GenericResponse<Void> update(@RequestBody List<RuntimeConfiguration> runtimeConfigurations) {
        if (runtimeConfigurations.isEmpty()) {
            return ResponseHelper.buildSuccessfulResponse();
        }
        runtimeConfigurationService.updateConfigurations(runtimeConfigurations);
        return ResponseHelper.buildSuccessfulResponse();
    }

    @RestrictAccess
    @PatchMapping("/reset/{subject}")
    public GenericResponse<Void> update(@PathVariable("subject") String subject) {
        runtimeConfigurationService.reset(subject);
        return ResponseHelper.buildSuccessfulResponse();
    }
}
