package com.onezol.vertex.framework.component.email.constant;

import com.onezol.vertex.framework.common.constant.enumeration.StandardEnumeration;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Schema(name = "邮件模版")
@Getter
@AllArgsConstructor
public enum MailTemplate implements StandardEnumeration<String> {

    LOGIN_VERIFICATION_CODE("登录验证码", "templates/mail/login-verification-code.html");

    @Schema(description = "模板名称")
    private final String name;

    @Schema(description = "模板路径")
    private final String value;

}
