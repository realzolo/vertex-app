package com.onezol.vertex.framework.common.model.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * API 错误日志
 */
@Schema(name = "ExceptionLogEntity", description = "API错误日志")
@Data
@EqualsAndHashCode(callSuper = true)
@TableName(value = "vx_exception_log")
public class ExceptionLogEntity extends BaseEntity {

    @Schema(description = "用户ID")
    @TableField(value = "user_id")
    private Long userId;

    @Schema(description = "请求方法名")
    @TableField(value = "request_method")
    private String requestMethod;

    @Schema(description = "访问地址")
    @TableField(value = "request_url")
    private String requestUrl;

    @Schema(description = "请求参数")
    @TableField(value = "request_params")
    private String requestParams;

    @Schema(description = "用户IP")
    @TableField(value = "user_ip")
    private String userIp;

    @Schema(description = "浏览器UA")
    @TableField(value = "user_agent")
    private String userAgent;

    @Schema(description = "异常名")
    @TableField(value = "exception_name")
    private String exceptionName;

    @Schema(description = "异常发生的类全名")
    @TableField(value = "exception_class_name")
    private String exceptionClassName;

    @Schema(description = "异常发生的类文件")
    @TableField(value = "exception_file_name")
    private String exceptionFileName;

    @Schema(description = "异常发生的方法名")
    @TableField(value = "exception_method_name")
    private String exceptionMethodName;

    @Schema(description = "异常发生的方法所在行")
    @TableField(value = "exception_line_number")
    private Integer exceptionLineNumber;

    @Schema(description = "异常的栈轨迹异常的栈轨迹")
    @TableField(value = "exception_stack_trace")
    private String exceptionStackTrace;

    @Schema(description = "异常导致的根消息")
    @TableField(value = "exception_root_cause_message")
    private String exceptionRootCauseMessage;

    @Schema(description = "异常导致的消息")
    @TableField(value = "exception_message")
    private String exceptionMessage;

}
