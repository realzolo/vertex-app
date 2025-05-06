package com.onezol.vertx.framework.support.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 通用线程池配置属性
 */
@Data
@Component
@ConfigurationProperties(prefix = "application.thread.pool")
public class ThreadPoolProperties {

    // 获取 CPU 核心数
    private static final int CPU_CORES = Runtime.getRuntime().availableProcessors();

    /**
     * 公共线程池核心线程数
     * 默认值为 CPU 核心数 / 2 + 1
     */
    private int corePoolSize = CPU_CORES / 2 + 1;

    /**
     * 异步任务线程池核心线程数
     * （默认 CPU 核心数 / 2）
     */
    private int scheduledCorePoolSize = CPU_CORES / 2;

    /**
     * 线程池允许的最大线程数，当任务队列满时，线程池会创建新的线程直到达到该数量。
     * 默认值为 CPU 核心数 * 2 + 1
     */
    private int maxPoolSize = CPU_CORES * 2 + 1;

    /**
     * 线程池任务队列的容量，用于存储等待执行的任务。
     * 默认值为 500
     */
    private int queueCapacity = 500;

    /**
     * 线程的空闲存活时间，当线程空闲时间超过该值时，多余的线程会被销毁。
     * 默认值为 60 秒
     */
    private int keepAliveSeconds = 60;

}
