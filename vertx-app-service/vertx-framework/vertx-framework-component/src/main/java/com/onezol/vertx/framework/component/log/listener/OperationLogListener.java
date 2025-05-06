package com.onezol.vertx.framework.component.log.listener;

import com.onezol.vertx.framework.common.annotation.LogPoint;
import com.onezol.vertx.framework.common.constant.StringConstants;
import com.onezol.vertx.framework.common.constant.enumeration.SuccessFailureStatus;
import com.onezol.vertx.framework.common.model.SharedHttpServletRequest;
import com.onezol.vertx.framework.common.util.JsonUtils;
import com.onezol.vertx.framework.common.util.NetworkUtils;
import com.onezol.vertx.framework.component.log.model.entity.OperationLogEntity;
import com.onezol.vertx.framework.component.log.service.OperationLogService;
import com.onezol.vertx.framework.security.api.context.UserIdentityContext;
import com.onezol.vertx.framework.security.api.model.UserIdentity;
import com.onezol.vertx.framework.support.event.OperationLoggedEvent;
import eu.bitwalker.useragentutils.UserAgent;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.context.event.EventListener;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.Objects;

/**
 * 操作日志监听器，用于监听操作日志事件并处理操作日志的持久化操作。
 * 该类会在接收到 {@link OperationLoggedEvent} 事件时，将操作日志信息保存到数据库中。
 */
@Slf4j
@Component
public class OperationLogListener {

    private final OperationLogService operationLogService;

    public OperationLogListener(OperationLogService operationLogService) {
        this.operationLogService = operationLogService;
    }

    /**
     * 监听操作日志事件，当接收到事件时调用 {@link #processOperationLog(JoinPoint, SharedHttpServletRequest, Object, long, Exception)} 方法处理日志。
     *
     * @param event 操作日志事件对象，包含请求、处理结果、耗时和异常等信息。
     */
    @EventListener(OperationLoggedEvent.class)
    public void onOperationLogEvent(OperationLoggedEvent event) {
        this.processOperationLog(
                event.getJoinPoint(),
                event.getSharedRequest(),
                event.getProcessingResult(),
                event.getProcessingTime(),
                event.getProcessingException()
        );
    }

    /**
     * 处理操作日志。
     *
     * @param joinPoint                AOP 切面中的连接点，用于获取方法调用的相关信息。
     * @param sharedHttpServletRequest 共享的 HTTP 请求对象，包含请求的详细信息。
     * @param result                   请求处理的结果对象。
     * @param time                     请求处理所花费的时间，单位为毫秒。
     * @param ex                       请求处理过程中抛出的异常，如果没有异常则为 null。
     */
    private void processOperationLog(JoinPoint joinPoint, SharedHttpServletRequest sharedHttpServletRequest, Object result, long time, Exception ex) {
        // 检查是否需要记录日志
        if (!this.shouldLogOperation(joinPoint)) {
            return;
        }
        try {
            // 提交日志到数据库
            this.saveOperationLogToDatabase(joinPoint, sharedHttpServletRequest, result, time, ex);
        } catch (Exception exp) {
            log.error("[操作日志] 记录访问日志失败", exp);
        }
    }

    /**
     * 将操作日志保存到数据库。
     *
     * @param joinPoint                AOP 切面中的连接点，用于获取方法调用的相关信息。
     * @param sharedHttpServletRequest 共享的 HTTP 请求对象，包含请求的详细信息。
     * @param result                   请求处理的结果对象。
     * @param time                     请求处理所花费的时间，单位为毫秒。
     * @param ex                       请求处理过程中抛出的异常，如果没有异常则为 null。
     */
    private void saveOperationLogToDatabase(JoinPoint joinPoint, SharedHttpServletRequest sharedHttpServletRequest, Object result, long time, Exception ex) {
        UserIdentity user = UserIdentityContext.get();
        if (user == null) {
            log.warn("[操作日志] 用户信息为空，无法记录完整操作日志");
            return;
        }

        // 获取请求参数并转换为 JSON 字符串
        Object[] args = joinPoint.getArgs();
        String params = "";
        try {
            params = JsonUtils.toJsonString(args);
        } catch (Exception jsonEx) {
            log.error("[操作日志] 请求参数 JSON 序列化失败", jsonEx);
        }

        String response = "";
        try {
            response = JsonUtils.toJsonString(result);
        } catch (Exception jsonEx) {
            log.error("[操作日志] 请求结果 JSON 序列化失败", jsonEx);
        }
        // 获取类名
        String className = joinPoint.getTarget().getClass().getName();
        // 获取方法名
        String methodName = joinPoint.getSignature().getName();
        // 拼接完整的操作方法名
        String operateMethod = className + "." + methodName;
        // 获取请求路径
        String requestURI = sharedHttpServletRequest.getRequestURI();

        // 获取请求信息（IP、地址、浏览器、操作系统）
        String ip = NetworkUtils.getHostIp();
        String location = NetworkUtils.getAddressByIP(ip);
        UserAgent userAgent = sharedHttpServletRequest.getUserAgent();
        String jsonUserAgent = JsonUtils.toJsonString(userAgent);
        String browser = userAgent.getBrowser().getName();
        String os = userAgent.getOperatingSystem().getName();

        // 获取异常信息，如果没有异常则为空字符串
        String failureReason = Objects.nonNull(ex) ? ExceptionUtils.getStackTrace(ex) : StringConstants.EMPTY;

        // 判断操作是否成功
        boolean success = !Objects.nonNull(ex);
        SuccessFailureStatus successFailureStatus = success ? SuccessFailureStatus.SUCCESS : SuccessFailureStatus.FAILURE;

        // 创建操作日志实体对象并设置属性
        OperationLogEntity entity = new OperationLogEntity();
        entity.setUserId(user.getUserId());
        entity.setRequestMethod(operateMethod);
        entity.setRequestUrl(requestURI);
        entity.setRequestParams(params);
        entity.setRequestResult(response);
        entity.setUserIp(ip);
        entity.setUserAgent(jsonUserAgent);
        entity.setLocation(location);
        entity.setBrowser(browser);
        entity.setOs(os);
        entity.setStatus(successFailureStatus);
        entity.setFailureReason(failureReason);
        entity.setTime(time);

        // 设置创建者和更新者信息
        entity.setCreator(user.getUserId());
        entity.setUpdater(user.getUserId());

        // 获取 Swagger 的 Operation 注解信息
        Operation operation = this.getOperation(joinPoint);
        if (Objects.nonNull(operation)) {
            // 设置操作行为
            entity.setAction(operation.summary());
            // 设置操作描述
            entity.setDescription(operation.description());
        }

        // 获取 Swagger 的 Tag 注解信息
        Tag tag = this.getTag(joinPoint);
        if (Objects.nonNull(tag)) {
            // 设置模块信息
            String module = tag.name();
            entity.setComponent(module);
        }

        // 保存操作日志实体到数据库
        try {
            operationLogService.save(entity);
        } catch (Exception dbEx) {
            log.error("[操作日志] 保存操作日志到数据库失败", dbEx);
        }
    }

    /**
     * 判断是否需要记录日志。通过检查方法或类上是否存在 {@link LogPoint} 注解，并且注解的 skip 属性为 false。
     *
     * @param joinPoint AOP 切面中的连接点，用于获取方法调用的相关信息。
     * @return 如果需要记录日志返回 true，否则返回 false。
     */
    private boolean shouldLogOperation(JoinPoint joinPoint) {
        // 获取方法签名
        Signature signature = joinPoint.getSignature();
        MethodSignature methodSignature = (MethodSignature) signature;
        Method method = methodSignature.getMethod();
        // 检查方法上是否存在 LogPoint 注解
        LogPoint annotation = method.getAnnotation(LogPoint.class);
        if (Objects.isNull(annotation)) {
            // 如果方法上不存在，则检查类上是否存在 LogPoint 注解
            annotation = AnnotationUtils.findAnnotation(method.getDeclaringClass(), LogPoint.class);
        }
        return annotation != null && !annotation.skip();
    }

    /**
     * 获取方法上的 {@link Operation} 注解。
     *
     * @param joinPoint AOP 切面中的连接点，用于获取方法调用的相关信息。
     * @return 如果方法上存在 Operation 注解则返回该注解对象，否则返回 null。
     */
    private Operation getOperation(JoinPoint joinPoint) {
        // 获取方法签名
        Signature signature = joinPoint.getSignature();
        MethodSignature methodSignature = (MethodSignature) signature;
        Method method = methodSignature.getMethod();
        // 获取方法上的 Operation 注解
        return method.getAnnotation(Operation.class);
    }

    /**
     * 获取类上的 {@link Tag} 注解。
     *
     * @param joinPoint AOP 切面中的连接点，用于获取方法调用的相关信息。
     * @return 如果类上存在 Tag 注解则返回该注解对象，否则返回 null。
     */
    private Tag getTag(JoinPoint joinPoint) {
        // 获取方法签名
        Signature signature = joinPoint.getSignature();
        MethodSignature methodSignature = (MethodSignature) signature;
        Method method = methodSignature.getMethod();
        // 获取类上的 Tag 注解
        return AnnotationUtils.findAnnotation(method.getDeclaringClass(), Tag.class);
    }

}
