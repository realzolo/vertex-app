package com.onezol.vertx.framework.component.log.model.dto;

import com.onezol.vertx.framework.common.constant.enumeration.SuccessFailureStatus;
import com.onezol.vertx.framework.common.skeleton.model.BaseDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Schema(description = "操作日志")
@Data
@EqualsAndHashCode(callSuper = true)
public class OperationLog extends BaseDTO {

    @Schema(description = "用户ID")
    private Long userId;

    @Schema(description = "模块名称")
    private String component;

    @Schema(description = "操作名称")
    private String action;

    @Schema(description = "操作描述")
    private String description;

    @Schema(description = "请求方法名")
    private String requestMethod;

    @Schema(description = "访问地址")
    private String requestUrl;

    @Schema(description = "请求参数")
    private String requestParams;

    @Schema(description = "请求结果")
    private String requestResult;

    @Schema(description = "用户IP")
    private String userIp;

    @Schema(description = "浏览器UA")
    private String userAgent;

    @Schema(description = "地理位置")
    private String location;

    @Schema(description = "浏览器类型")
    private String browser;

    @Schema(description = "操作系统")
    private String os;

    @Schema(description = "操作状态")
    private SuccessFailureStatus status;

    @Schema(description = "失败原因")
    private String failureReason;

    @Schema(description = "耗时（毫秒）")
    private Long time;

}
