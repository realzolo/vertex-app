package com.onezol.vertx.framework.security.api.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class LoginUser {

    @Schema(name = "用户ID")
    private Long userId;

    @Schema(name = "用户名称")
    private String username;

    @Schema(name = "用户昵称")
    private String nickname;

    @Schema(name = "用户头像")
    private String avatar;

    @Schema(name = "登陆方式")
    private String loginType;

    @Schema(name = "登录时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime loginTime;

    @Schema(name = "登录IP")
    private String ip;

    @Schema(name = "浏览器版本")
    private String browser;

    @Schema(name = "操作系统版本")
    private String os;

    @Schema(name = "登录地点")
    private String location;

    @Schema(name = "在线时长")
    private String onlineTime;

}
