package com.onezol.vertex.framework.common.constant.enumeration;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Schema(name = "服务状态码")
@Getter
@AllArgsConstructor
public enum ServiceStatus {

    /**
     * 操作或请求成功<br>
     * 当操作或请求成功完成时使用
     */
    SUCCESS(10200, HttpStatus.OK.getReasonPhrase()),

    /**
     * 请求参数错误或输入无效<br>
     * 当请求中包含无效参数、缺少必需的参数或参数格式不正确时，可以使用此异常来指示参数错误。
     */
    BAD_REQUEST(10400, "请求参数不正确"),

    /**
     * 用户未经授权或身份验证，无法执行请求的操作。<br>
     * 当用户尝试执行需要身份验证或授权的操作时，但未提供有效凭据或未通过身份验证时，可以使用此异常来指示其操作未经授权。
     */
    UNAUTHORIZED(10401, "未授权操作"),

    /**
     * 禁止访问请求的资源或操作<br>
     * 当用户尝试访问被明确禁止或限制的资源或执行受限制的操作时，可以使用此异常来指示其无法进行访问。
     */
    FORBIDDEN(10403, "无操作权限"),

    /**
     * 请求未找到<br>
     * 当请求中包含无效资源路径或无效标识符时，可以使用此异常来指示请求未找到。
     */
    NOT_FOUND(10404, "请求未找到"),

    /**
     * 请求方法不正确<br>
     * 当请求中包含无效方法或不支持的方法时，可以使用此异常来指示方法不正确。
     */
    METHOD_NOT_ALLOWED(10405, "请求方法不正确"),

    /**
     * 请求失败，请稍后重试<br>
     * 当该错误属于临时性且可能会在一段时间内解决时，可以使用此异常来指示请求失败。
     */
    LOCKED(10423, "请求失败，请稍后重试"),

    /**
     * 请求过于频繁，请稍后重试<br>
     * 当请求设置了速率限制，且超过了速率限制时，可以使用此异常来指示请求过于频繁。
     */
    TOO_MANY_REQUESTS(10429, "请求过于频繁，请稍后重试"),

    /**
     * 系统异常<br>
     * 其它的不可预知的异常，可以使用此异常来指示系统异常。
     */
    INTERNAL_SERVER_ERROR(10500, "系统内部异常"),

    /**
     * 功能未实现/未开启<br>
     * 当功能未实现或未开启时，可以使用此异常来指示功能未实现或未开启。
     */
    NOT_IMPLEMENTED(10501, "功能未实现/未开启"),

    /**
     * 错误的配置项<br>
     * 当配置项错误时，可以使用此异常来指示配置项错误。
     */
    ERROR_CONFIGURATION(10502, "错误的配置项"),

    /**
     * 重复请求<br>
     * 当请求重复时，可以使用此异常来指示重复请求。
     */
    REPEATED_REQUESTS(10900, "重复请求，请稍后重试"),

    ;

    private final Integer value;

    private final String description;

}
