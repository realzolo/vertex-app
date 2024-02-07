package com.onezol.vertex.framework.support.handler;

import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.extension.toolkit.Db;
import com.onezol.vertex.framework.common.bean.AuthenticationContext;
import com.onezol.vertex.framework.common.constant.enums.BizHttpStatus;
import com.onezol.vertex.framework.common.exception.BusinessException;
import com.onezol.vertex.framework.common.helper.ResponseHelper;
import com.onezol.vertex.framework.common.model.entity.ExceptionLogEntity;
import com.onezol.vertex.framework.common.model.pojo.AuthUserModel;
import com.onezol.vertex.framework.common.model.pojo.ResponseModel;
import com.onezol.vertex.framework.common.model.pojo.SharedHttpServletRequest;
import com.onezol.vertex.framework.common.util.JsonUtils;
import com.onezol.vertex.framework.support.manager.async.AsyncTaskManager;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.util.Assert;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.time.LocalDateTime;
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
        log.error(ex.getMessage(), ex);
        String message = "参数异常, 请检查请求参数";
        FieldError fieldError = ex.getBindingResult().getFieldError();
        if (Objects.nonNull(fieldError)) {
            message = fieldError.getDefaultMessage();
        }
        return ResponseHelper.buildFailedResponse(BizHttpStatus.BAD_REQUEST, message);
    }

    /**
     * NoResourceFoundException 处理资源未找到异常<br/>
     * 此处不做处理，直接抛出异常(因为存在兜底的异常处理器{@link #handleException}, 为避免被其处理导致静态资源404, 所以此处直接抛出)
     */
    @ExceptionHandler(value = NoResourceFoundException.class)
    public Object handleNoResourceFoundException(HttpServletRequest req, NoResourceFoundException ex) throws NoResourceFoundException {
        throw ex;
    }

    /**
     * NullPointerException 空指针异常
     */
    @ExceptionHandler(value = NullPointerException.class)
    public ResponseModel<?> handleNullPointerException(HttpServletRequest req, NullPointerException ex) {
        log.error(ex.getMessage(), ex);
        return ResponseHelper.buildFailedResponse(BizHttpStatus.INTERNAL_SERVER_ERROR, "系统内部错误，请联系管理员！");
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
        log.error(ex.getMessage(), ex);
        // API异常日志持久化
        SharedHttpServletRequest request = SharedHttpServletRequest.of(req);
        AsyncTaskManager.getInstance().execute(() -> this.createExceptionLog(request, ex));
        return ResponseHelper.buildFailedResponse(BizHttpStatus.INTERNAL_SERVER_ERROR.getCode(), ex.getMessage());
    }

    /**
     * API异常日志持久化
     *
     * @param req HttpServletRequest
     * @param ex  Exception
     */
    private void createExceptionLog(SharedHttpServletRequest req, Exception ex) {
        try {
            ExceptionLogEntity entity = this.newExceptionLog(req, ex);
            Db.save(entity);
        } catch (Exception exception) {
            log.error("[createExceptionLog]", exception);
        }
    }

    private ExceptionLogEntity newExceptionLog(SharedHttpServletRequest request, Throwable ex) {
        ExceptionLogEntity exLog = new ExceptionLogEntity();
        // 处理用户信息
        AuthUserModel authUserModel = AuthenticationContext.get();
        if (Objects.nonNull(authUserModel)) {
            exLog.setUserId(authUserModel.getUserId());
            exLog.setCreator(authUserModel.getUserCode());
            exLog.setCreateTime(LocalDateTime.now());
            exLog.setUpdater(authUserModel.getUserCode());
            exLog.setUpdateTime(LocalDateTime.now());
            exLog.setUserAgent(JSON.toJSONString(request.getUserAgent()));
            exLog.setUserIp(request.getRemoteAddr());
        }
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
    private Map<String, Object> getRequestPayload(SharedHttpServletRequest request) {
        Map<String, String[]> parameters = request.getParameters();
        Map<String, Object> body = request.getBody();
        // 如果requestBody的value存在MultipartFile类型的值，将其替换为文件名
        for (Map.Entry<String, Object> entry : body.entrySet()) {
            if (entry.getValue() instanceof MultipartFile multipartFile) {
                body.put(entry.getKey(), multipartFile.getOriginalFilename());
            }
        }
        return new LinkedHashMap<>(2) {{
            this.put("query", parameters);
            this.put("body", body);
        }};
    }

}
