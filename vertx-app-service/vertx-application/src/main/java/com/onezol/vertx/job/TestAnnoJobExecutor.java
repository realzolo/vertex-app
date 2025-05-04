package com.onezol.vertx.job;

import com.aizuda.snailjob.client.job.core.annotation.JobExecutor;
import com.aizuda.snailjob.client.job.core.dto.JobArgs;
import com.aizuda.snailjob.client.model.ExecuteResult;
import com.onezol.vertx.framework.common.util.ThreadUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class TestAnnoJobExecutor {

    @JobExecutor(name = "testJobExecutor")
    public ExecuteResult jobExecute(JobArgs jobArgs) {
        log.info("测试任务执行, 参数：{}", jobArgs.getExecutorInfo());
        ThreadUtils.sleep(5000);
        return ExecuteResult.success("测试成功");
    }

}