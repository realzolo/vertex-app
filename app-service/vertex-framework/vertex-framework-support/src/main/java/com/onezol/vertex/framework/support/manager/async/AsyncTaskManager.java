package com.onezol.vertex.framework.support.manager.async;

import com.onezol.vertex.framework.common.util.SpringUtils;
import com.onezol.vertex.framework.common.util.ThreadUtils;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * 异步任务管理器
 */
@Slf4j
public class AsyncTaskManager {

    @Getter
    private final static AsyncTaskManager instance = new AsyncTaskManager();
    private final ScheduledExecutorService executorService;

    private AsyncTaskManager() {
        executorService = SpringUtils.getBean("scheduledExecutorService");
        log.info("异步任务管理器初始化完成");
    }

    /**
     * 执行任务
     *
     * @param task 任务
     */
    public void execute(Runnable task) {
        int OPERATE_DELAY_TIME = 10;
        executorService.schedule(task, OPERATE_DELAY_TIME, TimeUnit.MILLISECONDS);
    }

    /**
     * 执行任务
     *
     * @param task  任务
     * @param delay 延迟时间(毫秒)
     */
    public void execute(Runnable task, long delay) {
        executorService.schedule(task, delay, TimeUnit.MILLISECONDS);
    }

    /**
     * 停止任务线程池
     */
    public void shutdown() {
        ThreadUtils.shutdownAndAwaitTermination(executorService);
    }

}

