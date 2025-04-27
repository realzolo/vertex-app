package com.onezol.vertex.framework.security.api.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class UserPassword {

    @Schema(description = "密码")
    private String password;

    @Schema(description = "密码过期时间")
    private LocalDate pwdExpDate;

}
