package com.onezol.vertx.framework.common.model.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Schema(name = "API错误日志")
@Data
@EqualsAndHashCode(callSuper = true)
@TableName(value = "app_exception_log")
public class ExceptionLogEntity extends BaseEntity {

    @Schema(name = "用户ID")
    @TableField(value = "user_id")
    private Long userId;

    @Schema(name = "请求方法名")
    @TableField(value = "request_method")
    private String requestMethod;

    @Schema(name = "访问地址")
    @TableField(value = "request_url")
    private String requestUrl;

    @Schema(name = "请求参数")
    @TableField(value = "request_params")
    private String requestParams;

    @Schema(name = "用户IP")
    @TableField(value = "user_ip")
    private String userIp;

    @Schema(name = "浏览器UA")
    @TableField(value = "user_agent")
    private String userAgent;

    @Schema(name = "异常名")
    @TableField(value = "exception_name")
    private String exceptionName;

    @Schema(name = "异常发生的类全名")
    @TableField(value = "exception_class_name")
    private String exceptionClassName;

    @Schema(name = "异常发生的类文件")
    @TableField(value = "exception_file_name")
    private String exceptionFileName;

    @Schema(name = "异常发生的方法名")
    @TableField(value = "exception_method_name")
    private String exceptionMethodName;

    @Schema(name = "异常发生的方法所在行")
    @TableField(value = "exception_line_number")
    private Integer exceptionLineNumber;

    @Schema(name = "异常的栈轨迹异常的栈轨迹")
    @TableField(value = "exception_stack_trace")
    private String exceptionStackTrace;

    @Schema(name = "异常导致的根消息")
    @TableField(value = "exception_root_cause_message")
    private String exceptionRootCauseMessage;

    @Schema(name = "异常导致的消息")
    @TableField(value = "exception_message")
    private String exceptionMessage;

}
