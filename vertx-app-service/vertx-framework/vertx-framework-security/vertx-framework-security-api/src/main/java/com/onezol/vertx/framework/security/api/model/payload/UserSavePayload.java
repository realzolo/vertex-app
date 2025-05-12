package com.onezol.vertx.framework.security.api.model.payload;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.List;

@Schema(name = "用户创建参数")
@Data
public class UserSavePayload {

    @Schema(name = "用户ID")
    private Long id;

    @Schema(name = "用户名")
    @NotBlank(message = "用户名不能为空")
    @Size(min = 4, max = 20, message = "用户名长度必须在4-20位之间")
    private String username;

    @Schema(name = "用户昵称")
    @NotBlank(message = "用户昵称不能为空")
    @Size(min = 1, max = 20, message = "用户昵称长度必须在1-20位之间")
    private String nickname;

    @Schema(name = "密码")
//    @NotBlank(message = "密码不能为空")
//    @Size(min = 6, max = 25, message = "密码长度必须在6-25位之间")
    private String password;

    @Schema(name = "手机号码")
    private String phone;

    @Schema(name = "电子邮箱")
    private String email;

    @Schema(name = "性别")
    private Integer gender;

    @Schema(name = "部门ID")
    private Long departmentId;

    @Schema(name = "角色Code列表")
    private List<String> roleCodes;

    @Schema(name = "备注")
    private String description;

    @Schema(name = "账户启用状态")
    private Integer status;

}
