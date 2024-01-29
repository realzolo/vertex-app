package com.onezol.vertex.framework.common.constant.enums;

import org.springframework.http.HttpStatus;

/**
 * 业务 HTTP 状态码
 */
public enum BizHttpStatus implements Enum {
    /**
     * 操作或请求成功<br>
     * 当操作或请求成功完成时使用
     */
    SUCCESS(HttpStatus.OK.getReasonPhrase(), 200),
    /**
     * 请求参数错误或输入无效<br>
     * 当请求中包含无效参数、缺少必需的参数或参数格式不正确时，可以使用此异常来指示参数错误。
     */
    BAD_REQUEST("请求参数不正确", 400),
    /**
     * 用户未经授权或身份验证，无法执行请求的操作。<br>
     * 当用户尝试执行需要身份验证或授权的操作时，但未提供有效凭据或未通过身份验证时，可以使用此异常来指示其操作未经授权。
     */
    UNAUTHORIZED("未授权操作", 401),
    /**
     * 禁止访问请求的资源或操作<br>
     * 当用户尝试访问被明确禁止或限制的资源或执行受限制的操作时，可以使用此异常来指示其无法进行访问。
     */
    FORBIDDEN("无操作权限", 403),
    /**
     * 请求未找到<br>
     * 当请求中包含无效资源路径或无效标识符时，可以使用此异常来指示请求未找到。
     */
    NOT_FOUND("请求未找到", 404),
    /**
     * 请求方法不正确<br>
     * 当请求中包含无效方法或不支持的方法时，可以使用此异常来指示方法不正确。
     */
    METHOD_NOT_ALLOWED("请求方法不正确", 405),
    /**
     * 请求失败，请稍后重试<br>
     * 当该错误属于临时性且可能会在一段时间内解决时，可以使用此异常来指示请求失败。
     */
    LOCKED("请求失败，请稍后重试", 423),
    /**
     * 请求过于频繁，请稍后重试<br>
     * 当请求设置了速率限制，且超过了速率限制时，可以使用此异常来指示请求过于频繁。
     */
    TOO_MANY_REQUESTS("请求过于频繁，请稍后重试", 429),
    /**
     * 系统异常<br>
     * 其它的不可预知的异常，可以使用此异常来指示系统异常。
     */
    INTERNAL_SERVER_ERROR("系统异常", 500),
    /**
     * 功能未实现/未开启<br>
     * 当功能未实现或未开启时，可以使用此异常来指示功能未实现或未开启。
     */
    NOT_IMPLEMENTED("功能未实现/未开启", 501),
    /**
     * 错误的配置项<br>
     * 当配置项错误时，可以使用此异常来指示配置项错误。
     */
    ERROR_CONFIGURATION("错误的配置项", 502),
    /**
     * 重复请求<br>
     * 当请求重复时，可以使用此异常来指示重复请求。
     */
    REPEATED_REQUESTS("重复请求，请稍后重试", 900),

    ;

    private final String value;
    private final int code;

    BizHttpStatus(String value, int code) {
        this.value = value;
        this.code = code;
    }

    @Override
    public int getCode() {
        return code;
    }

    @Override
    public String getValue() {
        return value;
    }

}
