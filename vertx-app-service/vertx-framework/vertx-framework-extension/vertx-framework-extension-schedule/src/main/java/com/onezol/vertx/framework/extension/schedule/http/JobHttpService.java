package com.onezol.vertx.framework.extension.schedule.http;

import com.aizuda.snailjob.common.core.model.Result;
import com.onezol.vertx.framework.extension.schedule.model.JobPageResult;
import com.onezol.vertx.framework.extension.schedule.model.dto.Job;
import com.onezol.vertx.framework.extension.schedule.model.payload.JobSavePayload;
import com.onezol.vertx.framework.extension.schedule.model.payload.JobStatusSavePayload;
import com.onezol.vertx.framework.extension.schedule.model.payload.JobTriggerPayload;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.*;

import java.util.List;
import java.util.Set;

/**
 * 任务 REST API
 */
@HttpExchange(url = "${snail-job.server.api.url}/job", accept = "application/json")
public interface JobHttpService {

    /**
     * 分页查询列表
     */
    @GetExchange("/page/list")
    JobPageResult<List<Job>> page(
            @RequestParam("page") Long page,
            @RequestParam("size") Long size,
            @RequestParam(value = "groupName", required = false) String groupName,
            @RequestParam(value = "jobName", required = false) String jobName,
            @RequestParam(value = "jobStatus", required = false) Integer jobStatus
    );

    /**
     * 新增
     */
    @PostExchange
    Result<Boolean> create(@RequestBody JobSavePayload payload);

    /**
     * 修改
     */
    @PutExchange
    Result<Boolean> update(@RequestBody JobSavePayload payload);

    /**
     * 修改状态
     */
    @PutExchange("/status")
    Result<Boolean> updateStatus(@RequestBody JobStatusSavePayload payload);

    /**
     * 删除
     */
    @DeleteExchange("/ids")
    Result<Boolean> delete(@RequestBody Set<Long> ids);

    /**
     * 执行
     */
    @PostExchange("/trigger")
    Result<Boolean> trigger(@RequestBody JobTriggerPayload payload);

}
