package com.onezol.vertx.framework.extension.schedule.controller;

import com.onezol.vertx.framework.common.model.GenericResponse;
import com.onezol.vertx.framework.common.model.PagePack;
import com.onezol.vertx.framework.extension.schedule.model.dto.Job;
import com.onezol.vertx.framework.extension.schedule.model.payload.JobSavePayload;
import com.onezol.vertx.framework.extension.schedule.model.payload.JobStatusSavePayload;
import com.onezol.vertx.framework.extension.schedule.model.payload.JobTriggerPayload;
import com.onezol.vertx.framework.extension.schedule.model.query.JobQuery;
import com.onezol.vertx.framework.extension.schedule.service.JobService;
import com.onezol.vertx.framework.support.support.ResponseHelper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "任务 API")
@RestController
@RequestMapping("/schedule/job")
public class JobController {

    private final JobService jobService;

    public JobController(JobService jobService) {
        this.jobService = jobService;
    }

    @Operation(summary = "分页查询任务列表", description = "分页查询任务列表")
    @GetMapping("/page")
    @PreAuthorize("@Security.hasPermission('schedule:job:list')")
    public GenericResponse<PagePack<Job>> page(JobQuery query) {
        PagePack<Job> pack = jobService.page(query);
        return ResponseHelper.buildSuccessfulResponse(pack);
    }

    @Operation(summary = "新增任务", description = "新增任务")
    @PostMapping
    @PreAuthorize("@Security.hasPermission('schedule:job:create')")
    public GenericResponse<Boolean> create(@RequestBody JobSavePayload payload) {
        boolean ok = jobService.create(payload);
        return ResponseHelper.buildSuccessfulResponse(ok);
    }

    @Operation(summary = "修改任务", description = "修改任务")
    @Parameter(name = "id", description = "ID", example = "1", in = ParameterIn.PATH)
    @PutMapping("/{id}")
    @PreAuthorize("@Security.hasPermission('schedule:job:update')")
    public GenericResponse<Boolean> update(@PathVariable("id") Long id, @RequestBody JobSavePayload payload) {
        boolean ok = jobService.update(payload, id);
        return ResponseHelper.buildSuccessfulResponse(ok);
    }

    @Operation(summary = "修改任务状态", description = "修改任务状态")
    @PatchMapping("/{id}/status")
    @PreAuthorize("@Security.hasPermission('schedule:job:update')")
    public GenericResponse<Boolean> updateStatus(@PathVariable("id") Long id, @Validated @RequestBody JobStatusSavePayload payload) {
        boolean ok = jobService.updateStatus(payload, id);
        return ResponseHelper.buildSuccessfulResponse(ok);
    }

    @Operation(summary = "删除任务", description = "删除任务")
    @Parameter(name = "id", description = "ID", example = "1", in = ParameterIn.PATH)
    @DeleteMapping("/{id}")
    @PreAuthorize("@Security.hasPermission('schedule:job:delete')")
    public GenericResponse<Boolean> delete(@PathVariable("id") Long id) {
        boolean ok = jobService.delete(id);
        return ResponseHelper.buildSuccessfulResponse(ok);
    }

    @Operation(summary = "执行任务", description = "执行任务")
    @Parameter(name = "id", description = "ID", example = "1", in = ParameterIn.PATH)
    @PostMapping("/trigger/{id}")
    @PreAuthorize("@Security.hasPermission('schedule:job:trigger')")
    public GenericResponse<Boolean> trigger(@PathVariable("id") Long id) {
        JobTriggerPayload payload = new JobTriggerPayload();
        payload.setJobId(id);
        boolean ok = jobService.trigger(payload);
        return ResponseHelper.buildSuccessfulResponse(ok);
    }

    @Operation(summary = "查询任务分组列表", description = "查询任务分组列表")
    @GetMapping("/group")
    @PreAuthorize("@Security.hasPermission('schedule:job:list')")
    public GenericResponse<List<String>> listGroup() {
        List<String> groups = jobService.listGroup();
        return ResponseHelper.buildSuccessfulResponse(groups);
    }

}
