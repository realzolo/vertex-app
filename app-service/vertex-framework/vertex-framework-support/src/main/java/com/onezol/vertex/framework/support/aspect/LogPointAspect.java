package com.onezol.vertex.framework.support.aspect;

import com.baomidou.mybatisplus.extension.toolkit.Db;
import com.onezol.vertex.framework.common.annotation.LogPoint;
import com.onezol.vertex.framework.common.constant.StringConstants;
import com.onezol.vertex.framework.common.constant.enumeration.SuccessFailureStatus;
import com.onezol.vertex.framework.common.model.SharedHttpServletRequest;
import com.onezol.vertex.framework.common.model.entity.OperationLogEntity;
import com.onezol.vertex.framework.common.util.EnumUtils;
import com.onezol.vertex.framework.common.util.JsonUtils;
import com.onezol.vertex.framework.common.util.NetworkUtils;
import com.onezol.vertex.framework.common.util.ServletUtils;
import com.onezol.vertex.framework.security.api.context.AuthenticationContext;
import com.onezol.vertex.framework.security.api.model.dto.AuthUser;
import com.onezol.vertex.framework.support.manager.async.AsyncTaskManager;
import eu.bitwalker.useragentutils.UserAgent;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

import java.lang.reflect.Method;
import java.util.Objects;

@Slf4j
@Aspect
@Component
public class LogPointAspect {

    @Pointcut("@annotation(com.onezol.vertex.framework.common.annotation.LogPoint) || @within(com.onezol.vertex.framework.common.annotation.LogPoint)")
    public void pointcut() {
    }

    @Around("pointcut()")
    public Object doAround(ProceedingJoinPoint joinPoint) throws Throwable {
        Object result = null;
        Exception exception = null;

        StopWatch stopWatch = new StopWatch();          // 开始
        stopWatch.start();
        try {
            result = joinPoint.proceed();
        } catch (Exception e) {
            exception = e;
        }
        stopWatch.stop();                               // 结束
        long time = stopWatch.getTotalTimeMillis();     // 耗时(单位：毫秒)

        // 处理日志
        HttpServletRequest request = ServletUtils.getRequest();
        SharedHttpServletRequest sharedHttpServletRequest = SharedHttpServletRequest.of(request);
        Object finalResult = result;
        Exception finalException = exception;
        AsyncTaskManager.getInstance().execute(() -> this.handleLog(joinPoint, sharedHttpServletRequest, finalResult, time, finalException));

        if (exception != null) {
            throw exception;
        }
        return result;
    }

    /**
     * 处理日志
     *
     * @param joinPoint                切点
     * @param sharedHttpServletRequest 请求
     * @param result                   返回结果
     * @param time                     耗时(单位：毫秒)
     * @param ex                       异常
     */
    private void handleLog(final JoinPoint joinPoint, final SharedHttpServletRequest sharedHttpServletRequest, final Object result, final long time, final Exception ex) {
        if (!isLogPoint(joinPoint)) {
            return;
        }
        try {
            this.submitLog(joinPoint, sharedHttpServletRequest, result, time, ex);
        } catch (Exception exp) {
            log.error("记录访问日志失败", exp);
        }
    }

    /**
     * 提交存储操作日志
     *
     * @param joinPoint                切点
     * @param sharedHttpServletRequest 请求
     * @param result                   返回结果
     * @param time                     耗时(单位：毫秒)
     * @param ex                       异常
     */
    private void submitLog(final JoinPoint joinPoint, final SharedHttpServletRequest sharedHttpServletRequest, final Object result, final long time, final Exception ex) {
        AuthUser user = AuthenticationContext.get();

        // 请求参数
        Object[] args = joinPoint.getArgs();
        String params = JsonUtils.toJsonString(args);
        // 请求结果
        String response = JsonUtils.toJsonString(result);
        // 类名
        String className = joinPoint.getTarget().getClass().getName();
        // 方法名
        String methodName = joinPoint.getSignature().getName();
        String operateMethod = className + "." + methodName;
        // 请求路径
        String requestURI = sharedHttpServletRequest.getRequestURI();

        // 请求信息(IP、地址、浏览器、操作系统)
        String ip = NetworkUtils.getHostIp();
        String location = NetworkUtils.getAddressByIP(ip);
        UserAgent userAgent = ServletUtils.getUserAgent();
        String jsonUserAgent = JsonUtils.toJsonString(userAgent);
        String browser = userAgent.getBrowser().getName();
        String os = userAgent.getOperatingSystem().getName();

        // 异常信息
        String failureReason = Objects.nonNull(ex) ? ExceptionUtils.getStackTrace(ex) : StringConstants.EMPTY;

        // 操作状态
        boolean success = !Objects.nonNull(ex);
        Integer successFailureStatusValue = Integer.valueOf(String.valueOf(success));
        SuccessFailureStatus successFailureStatus = EnumUtils.getEnumByValue(SuccessFailureStatus.class, successFailureStatusValue);

        OperationLogEntity entity = OperationLogEntity.builder()
                .userId(user.getUserId())
                .requestMethod(operateMethod)
                .requestUrl(requestURI)
                .requestParams(params)
                .requestResult(response)
                .userIp(ip)
                .userAgent(jsonUserAgent)
                .location(location)
                .browser(browser)
                .os(os)
                .status(successFailureStatus)
                .failureReason(failureReason)
                .time(time)
                .build();

        entity.setCreator(user.getUserId());
        entity.setUpdater(user.getUserId());

        Operation operation = this.getOperation(joinPoint);
        if (Objects.nonNull(operation)) {
            // 操作行为
            entity.setAction(operation.summary());
            // 操作描述
            entity.setDescription(operation.description());
        }

        Tag tag = this.getTag(joinPoint);
        if (Objects.nonNull(tag)) {
            // 模块
            String module = tag.name();
            entity.setComponent(module);
        }

        Db.save(entity);
    }

    /**
     * 判断是否需要记录日志
     */
    private boolean isLogPoint(JoinPoint joinPoint) {
        Signature signature = joinPoint.getSignature();
        MethodSignature methodSignature = (MethodSignature) signature;
        Method method = methodSignature.getMethod();
        LogPoint annotation = method.getAnnotation(LogPoint.class);
        if (Objects.isNull(annotation)) {
            annotation = AnnotationUtils.findAnnotation(method.getDeclaringClass(), LogPoint.class);
        }
        return !annotation.skip();
    }

    /**
     * 获取Operation注解
     *
     * @param joinPoint 切点
     * @return Operation注解
     */
    private Operation getOperation(JoinPoint joinPoint) {
        Signature signature = joinPoint.getSignature();
        MethodSignature methodSignature = (MethodSignature) signature;
        Method method = methodSignature.getMethod();
        return method.getAnnotation(Operation.class);
    }

    /**
     * 获取Tag注解
     *
     * @param joinPoint 切点
     * @return Tag注解
     */
    private Tag getTag(JoinPoint joinPoint) {
        Signature signature = joinPoint.getSignature();
        MethodSignature methodSignature = (MethodSignature) signature;
        Method method = methodSignature.getMethod();
        return AnnotationUtils.findAnnotation(method.getDeclaringClass(), Tag.class);
    }

}
