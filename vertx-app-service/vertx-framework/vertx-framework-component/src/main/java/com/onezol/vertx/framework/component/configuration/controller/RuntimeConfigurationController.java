package com.onezol.vertx.framework.component.configuration.controller;

import com.onezol.vertx.framework.common.constant.enumeration.ServiceStatus;
import com.onezol.vertx.framework.common.model.DataPairRecord;
import com.onezol.vertx.framework.common.model.DictionaryEntry;
import com.onezol.vertx.framework.common.model.GenericResponse;
import com.onezol.vertx.framework.component.configuration.model.RuntimeConfiguration;
import com.onezol.vertx.framework.component.configuration.service.RuntimeConfigurationService;
import com.onezol.vertx.framework.security.api.annotation.RestrictAccess;
import com.onezol.vertx.framework.security.api.context.AuthenticationContext;
import com.onezol.vertx.framework.support.support.ResponseHelper;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Locale;
import java.util.Objects;

@Tag(name = "运行时配置")
@RestController
@RequestMapping("/config")
public class RuntimeConfigurationController {

    // 无需鉴权的配置项
    private final List<String> WITHOUT_AUTH_CONFIGURATION_SUBJECTS = List.of(
            "SITE"
    );

    private final RuntimeConfigurationService runtimeConfigurationService;

    public RuntimeConfigurationController(RuntimeConfigurationService runtimeConfigurationService) {
        this.runtimeConfigurationService = runtimeConfigurationService;
    }

    @GetMapping("/{subject}/dict")
    public GenericResponse<List<DictionaryEntry>> getListAsLabelValue(@PathVariable("subject") String subject) {
        if (!WITHOUT_AUTH_CONFIGURATION_SUBJECTS.contains(subject.toUpperCase(Locale.ROOT))) {
            if (Objects.isNull(AuthenticationContext.get())) {
                return ResponseHelper.buildFailedResponse(ServiceStatus.UNAUTHORIZED);
            }
        }
        List<DictionaryEntry> options = runtimeConfigurationService.listConfigurationToDictionaryEntry(subject);
        return ResponseHelper.buildSuccessfulResponse(options);
    }

    @RestrictAccess
    @GetMapping("/list/{subject}")
    public GenericResponse<List<DataPairRecord>> getList(@PathVariable("subject") String subject) {
        List<DataPairRecord> configurations = runtimeConfigurationService.listConfigurations(subject);
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
