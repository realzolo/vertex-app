package com.onezol.vertx.framework.component.log.model.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.onezol.vertx.framework.common.constant.enumeration.SuccessFailureStatus;
import com.onezol.vertx.framework.common.skeleton.model.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Schema(description = "操作日志")
@Data
@EqualsAndHashCode(callSuper = true)
@TableName(value = "app_operation_log")
public class OperationLogEntity extends BaseEntity {

    @Schema(description = "用户ID")
    @TableField(value = "user_id")
    private Long userId;

    @Schema(description = "模块名称")
    @TableField(value = "component")
    private String component;

    @Schema(description = "操作名称")
    @TableField(value = "action")
    private String action;

    @Schema(description = "操作描述")
    @TableField(value = "description")
    private String description;

    @Schema(description = "请求方法名")
    @TableField(value = "request_method")
    private String requestMethod;

    @Schema(description = "访问地址")
    @TableField(value = "request_url")
    private String requestUrl;

    @Schema(description = "请求参数")
    @TableField(value = "request_params")
    private String requestParams;

    @Schema(description = "请求结果")
    @TableField(value = "request_result")
    private String requestResult;

    @Schema(description = "用户IP")
    @TableField(value = "user_ip")
    private String userIp;

    @Schema(description = "浏览器UA")
    @TableField(value = "user_agent")
    private String userAgent;

    @Schema(description = "地理位置")
    @TableField(value = "location")
    private String location;

    @Schema(description = "浏览器类型")
    @TableField(value = "browser")
    private String browser;

    @Schema(description = "操作系统")
    @TableField(value = "os")
    private String os;

    @Schema(description = "操作状态")
    @TableField(value = "status")
    private SuccessFailureStatus status;

    @Schema(description = "失败原因")
    @TableField(value = "failure_reason")
    private String failureReason;

    @Schema(description = "耗时（毫秒）")
    @TableField(value = "time")
    private Long time;

}
