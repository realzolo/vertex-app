package com.onezol.vertx.framework.support.event;

import com.onezol.vertx.framework.common.model.SharedHttpServletRequest;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.ApplicationEvent;

/**
 * 异常日志事件类，用于封装异常日志相关的信息。
 */
@Setter
@Getter
public class ExceptionLoggedEvent extends ApplicationEvent {

    /**
     * 共享的 HTTP 请求对象，包含发生异常时的请求信息。
     * 可以通过该对象获取请求的 URL、参数、头部信息等。
     */
    private SharedHttpServletRequest sharedRequest;

    /**
     * 发生的异常对象，包含异常的类型、消息和堆栈跟踪信息。
     */
    private Throwable occurredException;

    /**
     * 创建异常日志事件对象
     *
     * @param source 事件源对象，通常是触发异常的对象。
     * @param sharedRequest 共享的 HTTP 请求对象。
     * @param occurredException 发生的异常对象。
     */
    public ExceptionLoggedEvent(Object source, SharedHttpServletRequest sharedRequest, Throwable occurredException) {
        super(source);
        this.sharedRequest = sharedRequest;
        this.occurredException = occurredException;
    }

}
