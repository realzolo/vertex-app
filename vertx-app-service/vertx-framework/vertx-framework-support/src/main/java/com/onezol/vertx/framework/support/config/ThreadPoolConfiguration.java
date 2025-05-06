package com.onezol.vertx.framework.support.config;

import com.onezol.vertx.framework.common.util.ThreadUtils;
import com.onezol.vertx.framework.support.config.properties.ThreadPoolProperties;
import com.onezol.vertx.framework.support.support.NamedThreadFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * 线程池核心配置类，负责创建和管理项目中的通用线程池实例。
 * <p>
 * 该类通过注入 {@link ThreadPoolProperties} 读取线程池配置参数（如核心线程数、最大线程数等），
 * 并根据业务场景差异，分别创建：
 * <ul>
 *   <li>通用型线程池（{@link #threadPoolTaskExecutor()}）：用于处理普通异步任务</li>
 *   <li>定时任务线程池（{@link #scheduledExecutorService()}）：用于处理需要延迟/定时执行的任务</li>
 * </ul>
 */
@Configuration
public class ThreadPoolConfiguration {

    private final ThreadPoolProperties threadPoolProperties;

    public ThreadPoolConfiguration(ThreadPoolProperties threadPoolProperties) {
        this.threadPoolProperties = threadPoolProperties;
    }

    /**
     * 创建通用型线程池，用于处理无定时/延迟需求的普通异步任务。
     * <p>
     * 配置参数来源：
     * <ul>
     *   <li>核心线程数：{@link ThreadPoolProperties#getCorePoolSize()}</li>
     *   <li>最大线程数：{@link ThreadPoolProperties#getMaxPoolSize()}</li>
     *   <li>任务队列容量：{@link ThreadPoolProperties#getQueueCapacity()}</li>
     *   <li>空闲线程存活时间：{@link ThreadPoolProperties#getKeepAliveSeconds()}</li>
     * </ul>
     * 拒绝策略：使用 {@link ThreadPoolExecutor.CallerRunsPolicy}，由调用线程直接执行被拒绝的任务，避免任务丢失。
     *
     * @return 通用线程池实例
     */
    @Bean(name = "commonThreadPoolTaskExecutor")
    public ThreadPoolTaskExecutor threadPoolTaskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setThreadNamePrefix("common-thread-pool-");
        executor.setThreadGroupName("common-thread-group");
        executor.setMaxPoolSize(threadPoolProperties.getMaxPoolSize());
        executor.setCorePoolSize(threadPoolProperties.getCorePoolSize());
        executor.setQueueCapacity(threadPoolProperties.getQueueCapacity());
        executor.setKeepAliveSeconds(threadPoolProperties.getKeepAliveSeconds());
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        return executor;
    }

    /**
     * 创建定时任务线程池，支持延迟执行、周期性任务等场景。
     * <p>
     * 特性说明：
     * <ul>
     *   <li>线程命名：通过 {@link NamedThreadFactory} 生成带前缀的线程名（如 "async-task-thread-1"），便于日志追踪</li>
     *   <li>异常处理：通过 {@link ThreadUtils#printException(Runnable, Throwable)} 记录任务异常</li>
     * </ul>
     * 核心线程数来源：{@link ThreadPoolProperties#getScheduledCorePoolSize()}
     *
     * @return 定时任务线程池实例
     */
    @Bean(name = "scheduledExecutorService")
    public ScheduledExecutorService scheduledExecutorService() {
        NamedThreadFactory threadFactory = new NamedThreadFactory("async-task-thread");
        return new ScheduledThreadPoolExecutor(
                threadPoolProperties.getScheduledCorePoolSize(),
                threadFactory,
                new ThreadPoolExecutor.CallerRunsPolicy()
        ) {
            @Override
            protected void afterExecute(Runnable r, Throwable t) {
                super.afterExecute(r, t);
                ThreadUtils.printException(r, t);
            }
        };
    }

}
