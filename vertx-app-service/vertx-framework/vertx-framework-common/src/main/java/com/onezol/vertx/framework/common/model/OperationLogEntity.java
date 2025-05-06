package com.onezol.vertx.framework.common.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.onezol.vertx.framework.common.constant.enumeration.SuccessFailureStatus;
import com.onezol.vertx.framework.common.skeleton.model.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Schema(name = "OperationLogEntity", description ="操作日志")
@Data
@Builder
@EqualsAndHashCode(callSuper = true)
@TableName(value = "app_operation_log")
public class OperationLogEntity extends BaseEntity {

    @Schema(name ="用户ID")
    @TableField(value = "user_id")
    private Long userId;

    @Schema(name ="模块名称")
    @TableField(value = "component")
    private String component;

    @Schema(name ="操作名称")
    @TableField(value = "action")
    private String action;

    @Schema(name ="操作描述")
    @TableField(value = "description")
    private String description;

    @Schema(name ="请求方法名")
    @TableField(value = "request_method")
    private String requestMethod;

    @Schema(name ="访问地址")
    @TableField(value = "request_url")
    private String requestUrl;

    @Schema(name ="请求参数")
    @TableField(value = "request_params")
    private String requestParams;

    @Schema(name ="请求结果")
    @TableField(value = "request_result")
    private String requestResult;

    @Schema(name ="用户IP")
    @TableField(value = "user_ip")
    private String userIp;

    @Schema(name ="浏览器UA")
    @TableField(value = "user_agent")
    private String userAgent;

    @Schema(name ="地理位置")
    @TableField(value = "location")
    private String location;

    @Schema(name ="浏览器类型")
    @TableField(value = "browser")
    private String browser;

    @Schema(name ="操作系统")
    @TableField(value = "os")
    private String os;

    @Schema(name ="操作状态")
    @TableField(value = "status")
    private SuccessFailureStatus status;

    @Schema(name ="失败原因")
    @TableField(value = "failure_reason")
    private String failureReason;

    @Schema(name ="耗时（毫秒）")
    @TableField(value = "time")
    private Long time;

}
