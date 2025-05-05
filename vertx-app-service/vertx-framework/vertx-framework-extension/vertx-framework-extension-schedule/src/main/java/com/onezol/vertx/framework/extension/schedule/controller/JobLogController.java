package com.onezol.vertx.framework.extension.schedule.controller;

import com.onezol.vertx.framework.common.model.GenericResponse;
import com.onezol.vertx.framework.common.model.PagePack;
import com.onezol.vertx.framework.extension.schedule.model.JobInstanceLogPageResult;
import com.onezol.vertx.framework.extension.schedule.model.dto.JobInstance;
import com.onezol.vertx.framework.extension.schedule.model.dto.JobLog;
import com.onezol.vertx.framework.extension.schedule.model.query.JobInstanceLogQuery;
import com.onezol.vertx.framework.extension.schedule.model.query.JobInstanceQuery;
import com.onezol.vertx.framework.extension.schedule.model.query.JobLogQuery;
import com.onezol.vertx.framework.extension.schedule.service.JobLogService;
import com.onezol.vertx.framework.support.support.ResponseHelper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = " 任务 API")
@RestController
@RequestMapping("/schedule/log")
public class JobLogController {

    private final JobLogService jobLogService;

    public JobLogController(JobLogService jobLogService) {
        this.jobLogService = jobLogService;
    }

    @Operation(summary = "分页查询任务日志列表", description = "分页查询任务日志列表")
    @GetMapping
    @PreAuthorize("@Security.hasPermission('schedule:log:list')")
    public GenericResponse<PagePack<JobLog>> page(JobLogQuery query) {
        PagePack<JobLog> pack = jobLogService.page(query);
        return ResponseHelper.buildSuccessfulResponse(pack);
    }

    @Operation(summary = "停止任务", description = "停止任务")
    @Parameter(name = "id", description = "ID", example = "1", in = ParameterIn.PATH)
    @PostMapping("/stop/{id}")
    @PreAuthorize("@Security.hasPermission('schedule:log:stop')")
    public GenericResponse<Boolean> stop(@PathVariable("id") Long id) {
        boolean ok = jobLogService.stop(id);
        return ResponseHelper.buildSuccessfulResponse(ok);
    }

    @Operation(summary = "重试任务", description = "重试任务")
    @Parameter(name = "id", description = "ID", example = "1", in = ParameterIn.PATH)
    @PostMapping("/retry/{id}")
    @PreAuthorize("@Security.hasPermission('schedule:log:retry')")
    public GenericResponse<Boolean> retry(@PathVariable("id") Long id) {
        boolean ok = jobLogService.retry(id);
        return ResponseHelper.buildSuccessfulResponse(ok);
    }

    @Operation(summary = "查询任务实例列表", description = "查询任务实例列表")
    @GetMapping("/instance")
    @PreAuthorize("@Security.hasPermission('schedule:log:list')")
    public GenericResponse<List<JobInstance>> listInstance(
            @RequestParam(value = "jobId", required = false) Long jobId,
            @RequestParam(value = "taskBatchId", required = false) Long taskBatchId
    ) {
        JobInstanceQuery query = new JobInstanceQuery();
        query.setJobId(jobId);
        query.setTaskBatchId(taskBatchId);
        List<JobInstance> instances = jobLogService.listInstance(query);
        return ResponseHelper.buildSuccessfulResponse(instances);
    }

    @Operation(summary = "分页查询任务实例日志列表", description = "分页查询任务实例日志列表")
    @GetMapping("/instance/log")
    @PreAuthorize("@Security.hasPermission('schedule:log:list')")
    public GenericResponse<JobInstanceLogPageResult> pageInstanceLog(JobInstanceLogQuery query) {
        JobInstanceLogPageResult result = jobLogService.pageInstanceLog(query);
        return ResponseHelper.buildSuccessfulResponse(result);
    }

}
