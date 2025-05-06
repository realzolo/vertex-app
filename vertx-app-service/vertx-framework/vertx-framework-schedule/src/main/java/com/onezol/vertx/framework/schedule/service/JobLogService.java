package com.onezol.vertx.framework.schedule.service;

import com.aizuda.snailjob.common.core.model.Result;
import com.onezol.vertx.framework.common.model.PagePack;
import com.onezol.vertx.framework.schedule.http.JobBatchHttpService;
import com.onezol.vertx.framework.schedule.model.JobInstanceLogPageResult;
import com.onezol.vertx.framework.schedule.model.JobPageResult;
import com.onezol.vertx.framework.schedule.model.dto.JobInstance;
import com.onezol.vertx.framework.schedule.model.dto.JobLog;
import com.onezol.vertx.framework.schedule.model.query.JobInstanceLogQuery;
import com.onezol.vertx.framework.schedule.model.query.JobInstanceQuery;
import com.onezol.vertx.framework.schedule.model.query.JobLogQuery;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 任务日志业务实现
 */
@Service
public class JobLogService {

    private final JobBatchHttpService jobBatchHttpService;

    public JobLogService(JobBatchHttpService jobBatchHttpService) {
        this.jobBatchHttpService = jobBatchHttpService;
    }

    public PagePack<JobLog> page(JobLogQuery query) {
        JobPageResult<List<JobLog>> page = jobBatchHttpService.page(query.getPageNumber(), query.getPageSize(), query.getJobId(), query.getGroupName(), query.getJobName(), query.getTaskBatchStatus(), null);
        return PagePack.of(page.getData(), page.getTotal(), page.getPage(), page.getSize());
    }

    public boolean stop(Long id) {
        Result<Boolean> result = jobBatchHttpService.stop(id);
        return result.getData();
    }

    public boolean retry(Long id) {
        Result<Boolean> result = jobBatchHttpService.retry(id);
        return result.getData();
    }

    public List<JobInstance> listInstance(JobInstanceQuery query) {
        Result<List<JobInstance>> result = jobBatchHttpService.pageTask(query.getJobId(), query.getTaskBatchId());
        return result.getData();
    }

    public JobInstanceLogPageResult pageInstanceLog(JobInstanceLogQuery query) {
        Result<JobInstanceLogPageResult> result = jobBatchHttpService.pageLog(query.getJobId(), query.getTaskBatchId(), query.getTaskId(), query.getStartId(), query.getFromIndex(), query.getSize());
        return result.getData();
    }

}
