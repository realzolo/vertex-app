package com.onezol.vertx.framework.common.util;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.*;

/**
 * 线程相关工具类，提供线程操作、线程池管理、异常处理等功能。
 */
@Slf4j
public final class ThreadUtils {

    private ThreadUtils() {
        throw new IllegalStateException("Utility class cannot be instantiated");
    }

    /**
     * sleep等待
     *
     * @param milliseconds 等待时间（毫秒）
     */
    public static void sleep(long milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();  // 恢复中断状态，避免上层无法感知
            log.warn("Thread sleep interrupted, milliseconds: {}", milliseconds, e);  // 记录警告日志
        }
    }

    /**
     * 停止线程池
     * 先使用shutdown, 停止接收新任务并尝试完成所有已存在任务.
     * 如果超时, 则调用shutdownNow, 取消在workQueue中Pending的任务,并中断所有阻塞函数.
     * 如果仍然超时，则记录错误日志.
     */
    public static void shutdownAndAwaitTermination(ExecutorService pool) {
        if (pool != null && !pool.isShutdown()) {
            pool.shutdown();
            try {
                if (!pool.awaitTermination(120, TimeUnit.SECONDS)) {
                    pool.shutdownNow();
                    if (!pool.awaitTermination(120, TimeUnit.SECONDS)) {
                        log.error("Thread pool failed to terminate after shutdownNow, pool: {}", pool);
                    }
                }
            } catch (InterruptedException ie) {
                pool.shutdownNow();
                Thread.currentThread().interrupt();  // 恢复中断状态
                log.warn("Interrupted while terminating thread pool, pool: {}", pool, ie);
            }
        }
    }

    /**
     * 打印线程异常信息
     *
     * @param r 执行的任务
     * @param t 捕获的异常（可能为null）
     */
    public static void printException(Runnable r, Throwable t) {
        // 处理Future任务的隐式异常（如Callable执行失败）
        if (t == null && r instanceof Future<?> future) {
            try {
                future.get();  // 直接获取结果，触发潜在异常（afterExecute在任务完成后调用，无需检查isDone）
            } catch (CancellationException ce) {
                t = ce;
            } catch (ExecutionException ee) {
                t = ee.getCause();  // 获取原始异常
            } catch (InterruptedException ie) {
                Thread.currentThread().interrupt();  // 恢复中断状态
                t = ie;
            }
        }
        if (t != null) {
            log.error("Task execution exception, task: {}", r.getClass().getSimpleName(), t);
        }
    }

}
