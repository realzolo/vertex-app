package com.onezol.vertex.framework.component.email.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(description = "登录验证码邮件模板变量")
@Data
public class LoginVerificationCodeTemplateVariables {

    @Schema(description = "应用名称")
    private String applicationName;

    @Schema(description = "登录验证码")
    private String verificationCode;

    @Schema(description = "登录验证码有效期(分钟)")
    private Integer verificationCodeExpire;

    @Schema(description = "发送时间")
    private String sendTime;

}
