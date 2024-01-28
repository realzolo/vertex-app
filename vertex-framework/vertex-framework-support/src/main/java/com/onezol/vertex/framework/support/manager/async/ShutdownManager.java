package com.onezol.vertex.framework.support.manager.async;

import jakarta.annotation.PreDestroy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * 确保应用退出时能关闭后台线程
 */
@Slf4j
@Component
public class ShutdownManager {

    @PreDestroy
    public void destroy() {
        shutdownAsyncManager();
    }

    /**
     * 停止异步执行任务
     */
    private void shutdownAsyncManager() {
        try {
            AsyncTaskManager.getInstance().shutdown();
            log.info("关闭异步任务线程池");
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

}
