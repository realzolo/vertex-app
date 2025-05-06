package com.onezol.vertx.framework.support.event;

import com.onezol.vertx.framework.common.model.SharedHttpServletRequest;
import lombok.Getter;
import lombok.Setter;
import org.aspectj.lang.ProceedingJoinPoint;
import org.springframework.context.ApplicationEvent;

/**
 * 请求日志事件类，用于封装请求日志相关的信息
 */
@Setter
@Getter
public class OperationLoggedEvent extends ApplicationEvent {

    /**
     * 共享的 HTTP 请求对象，包含请求的详细信息
     */
    private SharedHttpServletRequest sharedRequest;

    /**
     * AOP 切面中的连接点，用于获取方法调用的相关信息
     */
    private ProceedingJoinPoint joinPoint;

    /**
     * 请求处理的结果对象
     */
    private Object processingResult;

    /**
     * 请求处理所花费的时间，单位为毫秒
     */
    private long processingTime;

    /**
     * 请求处理过程中抛出的异常，如果没有异常则为 null
     */
    private Exception processingException;

    /**
     * 初始化请求日志事件对象
     * 
     * @param source 事件源对象
     * @param sharedRequest 共享的 HTTP 请求对象
     * @param joinPoint AOP 切面中的连接点
     * @param processingResult 请求处理的结果对象
     * @param processingTime 请求处理所花费的时间
     * @param processingException 请求处理过程中抛出的异常
     */
    public OperationLoggedEvent(Object source, SharedHttpServletRequest sharedRequest, ProceedingJoinPoint joinPoint, Object processingResult, long processingTime, Exception processingException) {
        super(source);
        this.sharedRequest = sharedRequest;
        this.joinPoint = joinPoint;
        this.processingResult = processingResult;
        this.processingTime = processingTime;
        this.processingException = processingException;
    }

}
