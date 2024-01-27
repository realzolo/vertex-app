package com.onezol.vertex.framework.security.api.model.payload;

import com.onezol.vertex.framework.common.model.payload.Payload;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Schema(description = "用户注册参数")
@Data
public class UserRegistrationPayload implements Payload {

    @Schema(description = "用户名")
    @NotBlank(message = "用户名不能为空")
    @Size(min = 4, max = 20, message = "用户名长度必须在4-20位之间")
    private String username;

    @Schema(description = "密码")
    @NotBlank(message = "密码不能为空")
    @Size(min = 6, max = 25, message = "密码长度必须在6-25位之间")
    private String password;

    @Schema(description = "电子邮箱")
    @NotBlank(message = "邮箱不能为空")
    @Email(message = "邮箱格式不正确")
    private String email;

    @Schema(description = "验证码")
    @NotBlank(message = "验证码不能为空")
    @Size(min = 6, max = 6, message = "验证码错误")
    private String verifyCode;

}
