package com.onezol.vertex.framework.security.api.model.payload;


import com.onezol.vertex.framework.common.model.payload.Payload;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@Schema(name = "用户登录参数", description = "用户名+密码 或 邮箱+验证码")
public class UserLoginPayload implements Payload {

    @Schema(name = "用户名")
    private String username;

    @Schema(name = "密码")
    private String password;

    @Schema(name = "邮箱")
    private String email;

    @Schema(name = "会话ID")
    private String uuid;

    @Schema(name = "验证码")
    @NotBlank(message = "验证码不能为空")
    private String captcha;

}