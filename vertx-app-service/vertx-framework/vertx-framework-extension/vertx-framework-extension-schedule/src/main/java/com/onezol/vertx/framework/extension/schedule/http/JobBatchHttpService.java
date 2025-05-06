package com.onezol.vertx.framework.extension.schedule.http;

import com.aizuda.snailjob.common.core.model.Result;
import com.onezol.vertx.framework.extension.schedule.model.JobInstanceLogPageResult;
import com.onezol.vertx.framework.extension.schedule.model.JobPageResult;
import com.onezol.vertx.framework.extension.schedule.model.dto.JobInstance;
import com.onezol.vertx.framework.extension.schedule.model.dto.JobLog;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.annotation.PostExchange;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 任务批次 REST API
 */
@HttpExchange(url = "${snail-job.server.api.url}/job", accept = "application/json")
public interface JobBatchHttpService {

    /**
     * 分页查询列表
     */
    @GetExchange("/batch/list")
    JobPageResult<List<JobLog>> page(
            @RequestParam("page") Long page,
            @RequestParam("size") Long size,
            @RequestParam(value = "jobId", required = false) Long jobId,
            @RequestParam(value = "groupName", required = false) String groupName,
            @RequestParam(value = "jobName", required = false) String jobName,
            @RequestParam(value = "taskBatchStatus", required = false) Integer taskBatchStatus,
            @RequestParam(value = "datetimeRange", required = false) LocalDateTime[] datetimeRange
    );

    /**
     * 停止
     */
    @PostExchange("/batch/stop/{id}")
    Result<Boolean> stop(@PathVariable("id") Long id);

    /**
     * 重试
     */
    @PostExchange("/batch/retry/{id}")
    Result<Boolean> retry(@PathVariable("id") Long id);

    /**
     * 分页查询任务实例列表
     */
    @GetExchange("/task/list")
    JobPageResult<List<JobInstance>> pageTask(
            @RequestParam(value = "jobId", required = false) Long jobId,
            @RequestParam(value = "taskBatchId", required = false) Long taskBatchId
    );

    /**
     * 分页查询任务实例日志列表
     *
     * @return 响应信息
     */
    @GetExchange("/log/list")
    Result<JobInstanceLogPageResult> pageLog(
            @RequestParam(value = "jobId", required = false) Long jobId,
            @RequestParam(value = "taskBatchId", required = false) Long taskBatchId,
            @RequestParam(value = "taskId", required = false) Long taskId,
            @RequestParam(value = "startId", required = false) Integer startId,
            @RequestParam(value = "fromIndex", required = false) Integer fromIndex,
            @RequestParam(value = "size", required = false) Integer size
    );

}
