package com.onezol.vertx.framework.component.log.model.dto;

import com.onezol.vertx.framework.common.skeleton.model.BaseDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Schema(description = "API错误日志")
@Data
@EqualsAndHashCode(callSuper = true)
public class ExceptionLog extends BaseDTO {

    @Schema(description = "用户ID")
    private Long userId;

    @Schema(description = "请求方法名")
    private String requestMethod;

    @Schema(description = "访问地址")
    private String requestUrl;

    @Schema(description = "请求参数")
    private String requestParams;

    @Schema(description = "用户IP")
    private String userIp;

    @Schema(description = "浏览器UA")
    private String userAgent;

    @Schema(description = "异常名")
    private String exceptionName;

    @Schema(description = "异常发生的类全名")
    private String exceptionClassName;

    @Schema(description = "异常发生的类文件")
    private String exceptionFileName;

    @Schema(description = "异常发生的方法名")
    private String exceptionMethodName;

    @Schema(description = "异常发生的方法所在行")
    private Integer exceptionLineNumber;

    @Schema(description = "异常的栈轨迹异常的栈轨迹")
    private String exceptionStackTrace;

    @Schema(description = "异常导致的根消息")
    private String exceptionRootCauseMessage;

    @Schema(description = "异常导致的消息")
    private String exceptionMessage;

}
