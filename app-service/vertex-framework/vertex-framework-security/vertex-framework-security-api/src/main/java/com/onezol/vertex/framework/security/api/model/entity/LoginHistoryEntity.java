package com.onezol.vertex.framework.security.api.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.onezol.vertex.framework.common.model.entity.BaseEntity;
import com.onezol.vertex.framework.security.api.enumeration.LoginTypeEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;


@Data
@EqualsAndHashCode(callSuper = true)
@TableName("vx_login_history")
@Schema(name = "LoginHistoryEntity", description = "$!{table.comment}")
public class LoginHistoryEntity extends BaseEntity {

    @Schema(name = "用户ID")
    private Long userId;

    @Schema(name = "登陆方式")
    private LoginTypeEnum loginType;

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
