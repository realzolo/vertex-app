package com.onezol.vertex.framework.support.support;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Instant;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.locks.LockSupport;

/**
 * 紧凑型雪花ID生成器
 * <br>
 * 结构：28位时间戳（秒级） + 1位机器ID + 6位序列号
 */
public class CompactSnowflakeIdGenerator {
    private static final Logger logger = LoggerFactory.getLogger(CompactSnowflakeIdGenerator.class);

    // ----------------------------- 配置常量 -----------------------------
    private static final long EPOCH = Instant.parse("2025-01-01T00:00:00Z").getEpochSecond();
    private static final int TIMESTAMP_BITS = 28;  // 时间戳占28位（约8.5年）
    private static final int MACHINE_ID_BITS = 1;  // 机器ID占1位（0~1）
    private static final int SEQUENCE_BITS = 6;    // 序列号占6位（0~63）

    // ----------------------------- 计算常量 -----------------------------
    private static final long MAX_TIMESTAMP = (1L << TIMESTAMP_BITS) - 1;
    private static final long MAX_MACHINE_ID = (1L << MACHINE_ID_BITS) - 1;
    private static final long MAX_SEQUENCE = (1L << SEQUENCE_BITS) - 1;
    private static final int MACHINE_ID_SHIFT = SEQUENCE_BITS;
    private static final int TIMESTAMP_SHIFT = MACHINE_ID_BITS + SEQUENCE_BITS;

    // ----------------------------- 运行时状态 -----------------------------
    private static final long MAX_LOGICAL_CLOCK = 86400; // 逻辑时钟最大值（24小时秒数）
    private final AtomicLong logicalClock = new AtomicLong(0);
    private final AtomicLong totalGenerated = new AtomicLong(0);
    private final AtomicLong clockBackwardEvents = new AtomicLong(0);
    private final long machineId;
    private final AtomicReference<State> state = new AtomicReference<>(new State(-1L, 0L));

    public CompactSnowflakeIdGenerator(long machineId) {
        if (machineId < 0 || machineId > MAX_MACHINE_ID) {
            throw new IllegalArgumentException("机器ID超出范围 (0 ~ " + MAX_MACHINE_ID + ")");
        }
        this.machineId = machineId;
    }

    // ----------------------------- 核心逻辑 -----------------------------
    public long nextId() {
        while (true) {
            final State oldState = state.get();
            final long currentSecond = getCurrentSecond();
            final long timestamp = currentSecond - EPOCH;

            // 时间戳溢出检查
            if (timestamp > MAX_TIMESTAMP) {
                throw new TimestampOverflowException("时间戳溢出，请调整EPOCH或升级系统");
            }

            // 处理时钟回拨
            if (currentSecond < oldState.lastTimestamp) {
                handleClockBackwards(currentSecond, oldState.lastTimestamp);
                continue; // 重新尝试生成
            }

            // 时间窗口切换时重置序列号
            if (currentSecond > oldState.lastTimestamp) {
                final State newState = new State(currentSecond, 0);
                if (state.compareAndSet(oldState, newState)) {
                    totalGenerated.incrementAndGet();
                    return assembleId(currentSecond, 0);
                }
            }
            // 同一时间窗口递增序列号
            else {
                final long newSeq = oldState.sequence + 1;
                if (newSeq <= MAX_SEQUENCE) {
                    final State newState = new State(currentSecond, newSeq);
                    if (state.compareAndSet(oldState, newState)) {
                        totalGenerated.incrementAndGet();
                        return assembleId(currentSecond, newSeq);
                    }
                }
                // 序列号耗尽等待下一秒
                else {
                    waitForNextSecond(currentSecond);
                }
            }
        }
    }

    // ----------------------------- ID组装 -----------------------------
    private long assembleId(long currentSecond, long sequenceValue) {
        // 防御性校验
        assert sequenceValue <= MAX_SEQUENCE : "序列号溢出: " + sequenceValue;
        assert machineId <= MAX_MACHINE_ID : "无效机器ID: " + machineId;

        final long timestampPart = (currentSecond - EPOCH + logicalClock.get()) << TIMESTAMP_SHIFT;
        final long machinePart = machineId << MACHINE_ID_SHIFT;
        return timestampPart | machinePart | sequenceValue;
    }

    // ----------------------------- 时钟回拨处理 -----------------------------
    private void handleClockBackwards(long currentSecond, long lastTimestamp) {
        final long offset = lastTimestamp - currentSecond;
        clockBackwardEvents.incrementAndGet();

        // 严重回拨直接抛异常
        if (offset > 5) {
            logicalClock.updateAndGet(lc -> (lc + 1) % MAX_LOGICAL_CLOCK);
            logger.error("严重时钟回拨: offset={}s, logicalClock={}", offset, logicalClock.get());
            throw new ClockBackwardException("时钟回拨超过阈值: " + offset + "秒");
        }

        // 小范围回拨等待恢复
        logger.warn("时钟回拨: offset={}ms", offset * 1000);
        sleepPrecisely(offset * 1000 + 10);
    }

    // ----------------------------- 等待策略 -----------------------------
    private void waitForNextSecond(long currentSecond) {
        long now;
        do {
            LockSupport.parkNanos(1_000_000); // 1ms精准等待
            now = getCurrentSecond();
        } while (now == currentSecond);

        // 原子更新到新时间窗口
        state.set(new State(now, 0));
    }

    // ----------------------------- 工具方法 -----------------------------
    private long getCurrentSecond() {
        return Instant.now().getEpochSecond();
    }

    private void sleepPrecisely(long millis) {
        final long start = System.currentTimeMillis();
        long elapsed;
        do {
            LockSupport.parkNanos(1_000_000);
            elapsed = System.currentTimeMillis() - start;
        } while (elapsed < millis);
    }

    // ----------------------------- 监控接口 -----------------------------
    public Map<String, Long> getMetrics() {
        return Map.of(
                "totalGenerated", totalGenerated.get(),
                "clockBackwardEvents", clockBackwardEvents.get(),
                "logicalClock", logicalClock.get()
        );
    }

    // ----------------------------- 原子状态 -----------------------------
    private record State(long lastTimestamp, long sequence) {
    }

    // ----------------------------- 异常体系 -----------------------------
    public static class TimestampOverflowException extends RuntimeException {
        public TimestampOverflowException(String message) {
            super(message);
        }
    }

    public static class ClockBackwardException extends RuntimeException {
        public ClockBackwardException(String message) {
            super(message);
        }
    }

}