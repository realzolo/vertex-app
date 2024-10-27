package com.onezol.vertex.framework.common.constant;

public record DefaultPage() {

    /**
     * 默认页码
     */
    public static int DEFAULT_PAGE_NO = 1;
    /**
     * 默认页大小
     */
    public static int DEFAULT_PAGE_SIZE = 10;

    /**
     * 最大页大小
     */
    public static int MAX_PAGE_SIZE = 100;

    /**
     * 每页条数 - 不分页(常用于导出接口)
     */
    public static int PAGE_SIZE_NONE = -1;

}
