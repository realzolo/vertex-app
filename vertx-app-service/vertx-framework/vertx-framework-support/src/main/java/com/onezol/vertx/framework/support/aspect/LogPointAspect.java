package com.onezol.vertx.framework.support.aspect;

import com.onezol.vertx.framework.common.model.SharedHttpServletRequest;
import com.onezol.vertx.framework.common.util.ServletUtils;
import com.onezol.vertx.framework.support.event.OperationLoggedEvent;
import com.onezol.vertx.framework.support.manager.async.AsyncTaskManager;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

@Slf4j
@Aspect
@Component
public class LogPointAspect {

    private final ApplicationEventPublisher eventPublisher;

    public LogPointAspect(ApplicationEventPublisher eventPublisher) {
        this.eventPublisher = eventPublisher;
    }

    @Pointcut("@annotation(com.onezol.vertx.framework.common.annotation.LogPoint) || @within(com.onezol.vertx.framework.common.annotation.LogPoint)")
    public void pointcut() {
    }

    @Around("pointcut()")
    public Object doAround(ProceedingJoinPoint joinPoint) throws Throwable {
        Object result = null;
        Exception exception = null;

        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        try {
            result = joinPoint.proceed();
        } catch (Exception ex) {
            exception = ex;
        }
        stopWatch.stop();
        long processingTime = stopWatch.getTotalTimeMillis();     // 耗时(单位：毫秒)

        SharedHttpServletRequest request = SharedHttpServletRequest.from(ServletUtils.getRequest());

        OperationLoggedEvent event = new OperationLoggedEvent(this, request, joinPoint, result, processingTime, exception);
        AsyncTaskManager.getInstance().execute(() -> eventPublisher.publishEvent(event));

        if (exception != null) {
            throw exception;
        }
        return result;
    }

}
