package com.onezol.vertx.framework.common.constant;

public final class DefaultPage {

    private DefaultPage() {
    }

    /**
     * 默认页码
     */
    public static long DEFAULT_PAGE_NUMBER = 1;
    /**
     * 默认页大小
     */
    public static long DEFAULT_PAGE_SIZE = 10;

    /**
     * 最大页大小
     */
    public static long MAX_PAGE_SIZE = 100;

    /**
     * 每页条数 - 不分页(常用于导出接口)
     */
    public static long PAGE_SIZE_NONE = -1;

}
