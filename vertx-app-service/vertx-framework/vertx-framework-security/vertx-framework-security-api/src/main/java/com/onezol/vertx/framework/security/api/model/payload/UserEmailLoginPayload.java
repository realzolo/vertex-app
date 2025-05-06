package com.onezol.vertx.framework.security.api.model.payload;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.io.Serializable;

@Data
@Schema(name = "用户登录参数", description = "邮箱验证码登录参数")
public class UserEmailLoginPayload implements Serializable {

    @Schema(name = "邮箱")
    @NotBlank(message = "邮箱不能为空")
    private String email;

    @Schema(name = "验证码")
    @NotBlank(message = "验证码不能为空")
    private String verificationCode;

}
