package com.onezol.vertx.framework.support.interceptor;

/**
 * 过滤器拦截器执行顺序
 */
public final class RequestInterceptionOrder {

    private RequestInterceptionOrder() {
    }

    // -------------------------👇过滤器👇-------------------------
    /**
     * 受保护资源过滤器
     */
    public static final int PROTECTED_RESOURCE_FILTER_ORDER = 0;
    // -------------------------👆过滤器👆-------------------------

    // -------------------------👇拦截器👇-------------------------
    /**
     * 用户身份上下文拦截器
     */
    public static final int USER_IDENTITY_CONTEXT_INTERCEPTOR_ORDER = 101;
    // -------------------------👆拦截器👆-------------------------

}
