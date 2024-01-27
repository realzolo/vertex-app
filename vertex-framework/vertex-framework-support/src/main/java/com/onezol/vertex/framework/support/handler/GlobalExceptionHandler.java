package com.onezol.vertex.framework.support.handler;

import com.onezol.vertex.framework.common.constant.enums.BizHttpStatus;
import com.onezol.vertex.framework.common.exception.BusinessException;
import com.onezol.vertex.framework.common.model.entity.ApiExceptionLogEntity;
import com.onezol.vertex.framework.common.model.pojo.ResponseModel;
import com.onezol.vertex.framework.common.util.JsonUtils;
import com.onezol.vertex.framework.common.helper.ResponseHelper;
import com.onezol.vertex.framework.common.util.ServletUtils;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.util.Assert;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * MethodArgumentNotValidException 处理请求参数格式错误<br>
     * tips: 使用 @Validated 注解时, 参数校验失败会抛出此异常
     */
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public ResponseModel<?> handleMethodArgumentNotValidException(HttpServletRequest req, MethodArgumentNotValidException ex) {
        String message = "参数异常, 请检查请求参数";
        FieldError fieldError = ex.getBindingResult().getFieldError();
        if (Objects.nonNull(fieldError)) {
            message = fieldError.getDefaultMessage();
        }
        return ResponseHelper.buildFailedResponse(BizHttpStatus.BAD_REQUEST, message);
    }

    /**
     * BusinessException 处理业务异常
     */
    @ExceptionHandler(value = BusinessException.class)
    public ResponseModel<?> handleBusinessException(HttpServletRequest req, BusinessException ex) {
        return ResponseHelper.buildFailedResponse(ex.getCode(), ex.getMessage());
    }

    /**
     * Exception 兜底处理系统异常
     */
    @ExceptionHandler(value = Exception.class)
    public ResponseModel<?> handleException(HttpServletRequest req, Exception ex) {
        log.error("[DefaultExceptionHandler]", ex);
        return ResponseHelper.buildFailedResponse(BizHttpStatus.INTERNAL_SERVER_ERROR.getCode(), ex.getMessage());
    }

    /**
     * API异常日志持久化
     *
     * @param req HttpServletRequest
     * @param ex  Exception
     */
    private void createExceptionLog(HttpServletRequest req, Exception ex) {
        ApiExceptionLogEntity errorLog = new ApiExceptionLogEntity();
        try {
            initExceptionLog(errorLog, req, ex);
//            apiErrorLogFrameworkService.createApiErrorLog(errorLog);
        } catch (Throwable th) {
//            log.error("[createExceptionLog][url({}) log({}) 发生异常]", req.getRequestURI(), JsonUtils.toJsonString(errorLog), th);
        }
    }

    private void initExceptionLog(ApiExceptionLogEntity exLog, HttpServletRequest request, Throwable ex) throws IOException {
        // 处理用户信息
//        exLog.setUserId(WebFrameworkUtils.getLoginUserId(request));
        // 设置异常字段
        exLog.setExceptionName(ex.getClass().getName());
        exLog.setExceptionMessage(ExceptionUtils.getMessage(ex));
        exLog.setExceptionRootCauseMessage(ExceptionUtils.getRootCauseMessage(ex));
        exLog.setExceptionStackTrace(ExceptionUtils.getStackTrace(ex));
        StackTraceElement[] stackTraceElements = ex.getStackTrace();
        Assert.notEmpty(stackTraceElements, "异常 stackTraceElements 不能为空");
        StackTraceElement stackTraceElement = stackTraceElements[0];
        exLog.setExceptionClassName(stackTraceElement.getClassName());
        exLog.setExceptionFileName(stackTraceElement.getFileName());
        exLog.setExceptionMethodName(stackTraceElement.getMethodName());
        exLog.setExceptionLineNumber(stackTraceElement.getLineNumber());
        // 设置其它字段
        exLog.setRequestUrl(request.getRequestURI());
        Map<String, String> requestParam = ServletUtils.getRequestParam(request);
        Map<String, Object> requestBody = ServletUtils.getRequestBody(request);
        // 如果requestBody的value存在MultipartFile类型的值，将其替换为文件名
        for (Map.Entry<String, Object> entry : requestBody.entrySet()) {
            if (entry.getValue() instanceof org.springframework.web.multipart.MultipartFile) {
                requestBody.put(entry.getKey(), ((org.springframework.web.multipart.MultipartFile) entry.getValue()).getOriginalFilename());
            }
        }
        Map<String, Object> requestPayload = new LinkedHashMap<>(2) {{
            this.put("query", requestParam);
            this.put("body", requestBody);
        }};
        exLog.setRequestParams(JsonUtils.toJsonString(requestPayload));
        exLog.setRequestMethod(request.getMethod());
        exLog.setUserAgent(ServletUtils.getUserAgent());
        exLog.setUserIp(ServletUtils.getClientIP());
    }

}
