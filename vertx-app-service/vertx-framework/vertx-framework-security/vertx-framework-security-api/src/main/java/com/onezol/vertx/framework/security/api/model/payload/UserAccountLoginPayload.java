package com.onezol.vertx.framework.security.api.model.payload;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.io.Serializable;

@Data
@Schema(name = "用户登录参数", description = "用户名密码登录参数")
public class UserAccountLoginPayload implements Serializable {

    @Schema(name = "用户名")
    @NotBlank(message = "用户名不能为空")
    private String username;

    @Schema(name = "密码")
    @NotBlank(message = "密码不能为空")
    private String password;

    @Schema(name = "用户指纹")
    @NotBlank(message = "用户指纹不能为空")
    private String fingerprint;

    @Schema(name = "验证码")
    @NotBlank(message = "验证码不能为空")
    private String verificationCode;

}
