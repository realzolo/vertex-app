package com.onezol.vertx.framework.component.log.listener;

import com.alibaba.fastjson2.JSON;
import com.onezol.vertx.framework.common.model.SharedHttpServletRequest;
import com.onezol.vertx.framework.common.util.JsonUtils;
import com.onezol.vertx.framework.component.log.model.entity.ExceptionLogEntity;
import com.onezol.vertx.framework.component.log.service.ExceptionLogService;
import com.onezol.vertx.framework.security.api.context.UserIdentityContext;
import com.onezol.vertx.framework.security.api.model.UserIdentity;
import com.onezol.vertx.framework.support.event.ExceptionLoggedEvent;
import com.onezol.vertx.framework.support.manager.async.AsyncTaskManager;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.context.event.EventListener;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

/**
 * 异常日志监听器，用于监听异常日志事件并处理异常日志的持久化操作。
 * 该类会在接收到 {@link ExceptionLoggedEvent} 事件时，将操作日志信息保存到数据库中。
 */
@Component
public class ExceptionLogListener {

    private final ExceptionLogService exceptionLogService;

    public ExceptionLogListener(ExceptionLogService exceptionLogService) {
        this.exceptionLogService = exceptionLogService;
    }

    @EventListener(ExceptionLoggedEvent.class)
    public void onExceptionLoggedEvent(ExceptionLoggedEvent event) {
        // 从事件中获取共享的 HTTP 请求对象和发生的异常对象
        SharedHttpServletRequest request = event.getSharedRequest();
        Throwable exception = event.getOccurredException();

        // 从上下文中获取当前用户身份信息
        UserIdentity userIdentity = UserIdentityContext.get();

        // 将异常日志处理任务提交到异步任务管理器执行
        AsyncTaskManager.getInstance().execute(() -> this.persistExceptionLog(userIdentity, request, exception));
    }

    /**
     * 持久化 API 异常日志到数据库
     *
     * @param request   HttpServletRequest 包装器
     * @param exception Exception
     */
    private void persistExceptionLog(@Nullable UserIdentity userIdentity, @NonNull SharedHttpServletRequest request, @NonNull Throwable exception) {
        ExceptionLogEntity entity = this.buildExceptionLogEntity(userIdentity, request, exception);
        exceptionLogService.save(entity);
    }

    /**
     * 创建异常日志实体对象
     *
     * @param request   HttpServletRequest 包装器
     * @param exception Exception
     * @return 异常日志实体对象
     */
    private ExceptionLogEntity buildExceptionLogEntity(@Nullable UserIdentity userIdentity, @NonNull SharedHttpServletRequest request, @NonNull Throwable exception) {
        ExceptionLogEntity exLog = new ExceptionLogEntity();

        // 处理用户信息
        if (Objects.nonNull(userIdentity)) {
            exLog.setUserId(userIdentity.getUserId());
            exLog.setCreator(userIdentity.getUserId());
            exLog.setCreateTime(LocalDateTime.now());
            exLog.setUpdater(userIdentity.getUserId());
            exLog.setUpdateTime(LocalDateTime.now());
            exLog.setUserAgent(JSON.toJSONString(request.getUserAgent()));
            exLog.setUserIp(request.getRemoteAddr());
        }

        // 设置异常字段
        exLog.setExceptionName(exception.getClass().getName());
        exLog.setExceptionMessage(ExceptionUtils.getMessage(exception));
        exLog.setExceptionRootCauseMessage(ExceptionUtils.getRootCauseMessage(exception));
        exLog.setExceptionStackTrace(ExceptionUtils.getStackTrace(exception));

        // 检查异常堆栈跟踪信息是否为空，避免数组越界异常
        StackTraceElement[] stackTraceElements = exception.getStackTrace();
        if (stackTraceElements != null && stackTraceElements.length > 0) {
            StackTraceElement stackTraceElement = stackTraceElements[0];
            exLog.setExceptionClassName(stackTraceElement.getClassName());
            exLog.setExceptionFileName(stackTraceElement.getFileName());
            exLog.setExceptionMethodName(stackTraceElement.getMethodName());
            exLog.setExceptionLineNumber(stackTraceElement.getLineNumber());
        }

        // 设置其它字段
        exLog.setRequestUrl(request.getRequestURI());
        Map<String, Object> requestPayload = this.getRequestPayload(request);
        exLog.setRequestParams(JsonUtils.toJsonString(requestPayload));
        exLog.setRequestMethod(request.getMethod());

        return exLog;
    }

    /**
     * 获取请求参数
     *
     * @param request 请求
     * @return 请求参数
     */
    private Map<String, Object> getRequestPayload(@NonNull SharedHttpServletRequest request) {
        // 获取请求的查询参数
        Map<String, String[]> parameters = request.getParameters();
        // 获取请求的主体内容
        Map<String, Object> body = request.getBody();

        // 如果 requestBody 的 value 存在 MultipartFile 类型的值，将其替换为文件名
        if (body != null) {
            for (Map.Entry<String, Object> entry : body.entrySet()) {
                if (entry.getValue() instanceof MultipartFile multipartFile) {
                    body.put(entry.getKey(), multipartFile.getOriginalFilename());
                }
            }
        }

        // 返回包含查询参数和请求主体的映射
        return new LinkedHashMap<>(2) {{
            this.put("query", parameters);
            this.put("body", body);
        }};
    }

}
