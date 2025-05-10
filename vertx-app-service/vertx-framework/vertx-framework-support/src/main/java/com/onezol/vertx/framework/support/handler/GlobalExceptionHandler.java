package com.onezol.vertx.framework.support.handler;

import com.onezol.vertx.framework.common.constant.enumeration.ServiceStatus;
import com.onezol.vertx.framework.common.exception.ServiceException;
import com.onezol.vertx.framework.common.model.GenericResponse;
import com.onezol.vertx.framework.common.model.SharedHttpServletRequest;
import com.onezol.vertx.framework.support.event.ExceptionLoggedEvent;
import com.onezol.vertx.framework.support.support.ResponseHelper;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.util.Objects;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    private final ApplicationEventPublisher eventPublisher;

    public GlobalExceptionHandler(ApplicationEventPublisher eventPublisher) {
        this.eventPublisher = eventPublisher;
    }


    /**
     * MethodArgumentNotValidException 处理请求参数格式错误<br>
     * tips: 使用 @Validated 注解时, 参数校验失败会抛出此异常
     */
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public GenericResponse<?> handleMethodArgumentNotValidException(HttpServletRequest req, MethodArgumentNotValidException ex) {
        log.error(ex.getMessage(), ex);
        String message = "参数异常, 请检查请求参数";
        FieldError fieldError = ex.getBindingResult().getFieldError();
        if (Objects.nonNull(fieldError)) {
            message = fieldError.getDefaultMessage();
        }
        return ResponseHelper.buildFailedResponse(ServiceStatus.BAD_REQUEST, message);
    }

    /**
     * NoResourceFoundException 处理资源未找到异常
     */
    @ExceptionHandler(value = NoResourceFoundException.class)
    public Object handleNoResourceFoundException(HttpServletRequest req, NoResourceFoundException ex) throws NoResourceFoundException {
        return ResponseHelper.buildFailedResponse(ServiceStatus.NOT_FOUND, ex.getMessage());
    }

    /**
     * MaxUploadSizeExceededException 文件上传大小异常
     */
    @ExceptionHandler(value = MaxUploadSizeExceededException.class)
    public GenericResponse<?> handleNullPointerException(HttpServletRequest req, MaxUploadSizeExceededException ex) {
        log.error(ex.getMessage(), ex);
        return ResponseHelper.buildFailedResponse(ServiceStatus.INTERNAL_SERVER_ERROR, "上传文件大小超限！");
    }

    /**
     * AccessDeniedException 处理权限不足异常
     */
    @ExceptionHandler(value = AccessDeniedException.class)
    public void handleAccessDeniedException(AccessDeniedException ex) {
        // 此处不做处理，让 UserAccessDeniedHandler 处理
        throw ex;
    }

    /**
     * ServiceException 处理服务异常
     */
    @ExceptionHandler(value = ServiceException.class)
    public GenericResponse<?> handleServiceException(HttpServletRequest req, ServiceException ex) {
        return ResponseHelper.buildFailedResponse(ex.getCode(), ex.getMessage());
    }

    /**
     * Exception 兜底处理系统异常
     */
    @ExceptionHandler(value = Exception.class)
    public GenericResponse<?> handleException(HttpServletRequest req, Exception ex) {
        log.error(ex.getMessage(), ex);

        ExceptionLoggedEvent event = new ExceptionLoggedEvent(this, SharedHttpServletRequest.from(req), ex);
        eventPublisher.publishEvent(event);

        String message = Objects.nonNull(ex.getMessage()) ? ex.getMessage() : ServiceStatus.INTERNAL_SERVER_ERROR.getDescription();
        return ResponseHelper.buildFailedResponse(ServiceStatus.INTERNAL_SERVER_ERROR.getValue(), message);
    }

}
