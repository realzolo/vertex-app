package com.onezol.vertx.framework.security.api.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.onezol.vertx.framework.common.skeleton.model.BaseEntity;
import com.onezol.vertx.framework.security.api.enumeration.LoginType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;


@Data
@EqualsAndHashCode(callSuper = true)
@TableName("app_login_history")
@Schema(name = "LoginHistoryEntity", description = "$!{table.comment}")
public class LoginHistoryEntity extends BaseEntity {

    @Schema(name = "用户ID")
    private Long userId;

    @Schema(name = "登陆方式")
    private LoginType loginType;

    @Schema(name = "登录时间")
    private LocalDateTime loginTime;

    @Schema(name = "登录IP")
    private String ip;

    @Schema(name = "浏览器版本")
    private String browser;

    @Schema(name = "操作系统版本")
    private String os;

    @Schema(name = "登录地点")
    private String location;

}
