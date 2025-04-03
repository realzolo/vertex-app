package com.onezol.vertex.framework.support.aspect;

import com.onezol.vertex.framework.common.annotation.TimeLog;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

/**
 * 切面: 打印方法执行时间
 */
@Slf4j
@Aspect
@Component
public class TimeLogAspect {

    @Pointcut("@annotation(com.onezol.vertex.framework.common.annotation.TimeLog)")
    public void pointcut() {
    }

    @Around("pointcut()")
    public Object timeLogAround(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();

        String className = signature.getMethod().getDeclaringClass().getName();
        String methodName = signature.getMethod().getName();

        StopWatch stopWatch = new StopWatch(className);
        stopWatch.start(methodName);

        Object result = joinPoint.proceed();

        stopWatch.stop();

        log.info("[{}] execute method {}() takes {}ms", className, methodName, stopWatch.getTotalTimeMillis());

        return result;
    }

}
