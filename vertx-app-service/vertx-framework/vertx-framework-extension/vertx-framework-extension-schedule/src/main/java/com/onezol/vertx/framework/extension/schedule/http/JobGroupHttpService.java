package com.onezol.vertx.framework.extension.schedule.http;

import com.aizuda.snailjob.common.core.model.Result;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;

import java.util.List;

/**
 * 任务组 REST API
 */
@HttpExchange(url = "${snail-job.server.api.url}/group", accept = "application/json")
public interface JobGroupHttpService {

    /**
     * 查询分组列表
     */
    @GetExchange("/all/group-name/list")
    Result<List<String>> listGroup();

}
