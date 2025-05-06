package com.onezol.vertx.framework.support.support;


import org.springframework.lang.NonNull;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 自定义线程工厂，用于生成具有指定名称前缀的线程。
 * <p>
 * 该工厂实现了 {@link ThreadFactory} 接口，通过构造时传入的线程名称前缀，
 * 为每个新创建的线程生成唯一的名称（格式：前缀-序号，如 "async-task-1"），
 * 方便通过日志或监控工具快速识别线程来源。
 */
public class NamedThreadFactory implements ThreadFactory {

    /**
     * 线程名称的前缀，用于标识线程的来源或用途（如 "async-task"）
     */
    private final String prefix;

    /**
     * 原子计数器，用于生成线程名称的递增序号（如前缀为 "async-task" 时，线程名称为 "async-task-1"、"async-task-2" 等）
     */
    private final AtomicInteger counter = new AtomicInteger(1);

    /**
     * 构造方法，初始化线程名称前缀
     *
     * @param prefix 线程名称的前缀（不可为空）
     */
    public NamedThreadFactory(String prefix) {
        this.prefix = prefix;
    }

    /**
     * 创建一个新线程，并设置其名称为 [prefix]-[序号]
     *
     * @param runnable 需要在线程中执行的任务
     * @return 新创建的线程实例
     */
    @Override
    public Thread newThread(@NonNull Runnable runnable) {
        Thread thread = new Thread(runnable);
        thread.setName(prefix + "-" + counter.getAndIncrement());
        return thread;
    }

}
