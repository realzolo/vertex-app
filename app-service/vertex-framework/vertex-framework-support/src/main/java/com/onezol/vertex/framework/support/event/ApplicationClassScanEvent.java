package com.onezol.vertex.framework.support.event;

import lombok.Getter;
import lombok.Setter;
import org.springframework.context.ApplicationEvent;

@Setter
@Getter
public class ApplicationClassScanEvent extends ApplicationEvent {

    /**
     * 全部类
     */
    private Class<?>[] classes;

    /**
     * Interface 类
     */
    private Class<?>[] interfaces;

    /**
     * Enum 类
     */
    private Class<?>[] enums;

    /**
     * Exception 类
     */
    private Class<?>[] exceptions;

    /**
     * Record 类
     */
    private Class<?>[] records;

    /**
     * Annotation 类
     */
    private Class<?>[] annotations;

    public ApplicationClassScanEvent(Object source) {
        super(source);
    }

}
