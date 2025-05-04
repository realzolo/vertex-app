package com.onezol.vertx.framework.extension.schedule.service;

import com.aizuda.snailjob.common.core.model.Result;
import com.onezol.vertx.framework.common.model.PagePack;
import com.onezol.vertx.framework.extension.schedule.http.JobGroupHttpService;
import com.onezol.vertx.framework.extension.schedule.http.JobHttpService;
import com.onezol.vertx.framework.extension.schedule.model.JobPageResult;
import com.onezol.vertx.framework.extension.schedule.model.dto.Job;
import com.onezol.vertx.framework.extension.schedule.model.payload.JobSavePayload;
import com.onezol.vertx.framework.extension.schedule.model.payload.JobStatusSavePayload;
import com.onezol.vertx.framework.extension.schedule.model.payload.JobTriggerPayload;
import com.onezol.vertx.framework.extension.schedule.model.query.JobQuery;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class JobService {

    private final JobHttpService jobHttpService;
    private final JobGroupHttpService jobGroupHttpService;

    public JobService(JobHttpService jobHttpService, JobGroupHttpService jobGroupHttpService) {
        this.jobHttpService = jobHttpService;
        this.jobGroupHttpService = jobGroupHttpService;
    }

    public PagePack<Job> page(JobQuery query) {
        JobPageResult<List<Job>> page = jobHttpService.page(query.getPage(), query.getSize(), query.getGroupName(), query.getJobName(), query.getJobStatus());
        return PagePack.of(page.getData(), page.getTotal(), page.getPage(), page.getSize());
    }

    public boolean create(JobSavePayload payload) {
        Result<Boolean> result = jobHttpService.create(payload);
        return result.getData();
    }

    public boolean update(JobSavePayload payload, Long id) {
        payload.setId(id);
        Result<Boolean> result = jobHttpService.update(payload);
        return result.getData();
    }

    public boolean updateStatus(JobStatusSavePayload payload, Long id) {
        payload.setId(id);
        Result<Boolean> result = jobHttpService.updateStatus(payload);
        return result.getData();
    }

    public boolean delete(Long id) {
        Result<Boolean> result = jobHttpService.delete(Collections.singleton(id));
        return result.getData();
    }

    public boolean trigger(JobTriggerPayload payload) {
        Result<Boolean> result = jobHttpService.trigger(payload);
        return result.getData();
    }

    public List<String> listGroup() {
        Result<List<String>> result = jobGroupHttpService.listGroup();
        return result.getData();
    }

}
