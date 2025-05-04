package com.onezol.vertx.framework.extension.schedule.config;

import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.spi.ILoggingEvent;
import com.aizuda.snailjob.client.common.appender.SnailLogbackAppender;
import com.aizuda.snailjob.client.common.event.SnailClientStartingEvent;
import com.aizuda.snailjob.client.starter.EnableSnailJob;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;

/**
 * Snail Job 配置
 */
@Configuration
@EnableSnailJob
@ConditionalOnProperty(prefix = "snail-job", value = "enabled", havingValue = "true", matchIfMissing = true)
public class SnailJobConfiguration {

    /**
     * 日志上报
     */
    @EventListener(SnailClientStartingEvent.class)
    public void onStarting() {
        SnailLogbackAppender<ILoggingEvent> appender = new SnailLogbackAppender<>();
        appender.start();
        LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();
        Logger rootLogger = loggerContext.getLogger(Logger.ROOT_LOGGER_NAME);
        rootLogger.addAppender(appender);
    }

}