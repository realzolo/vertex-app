package com.onezol.vertex.framework.security.api.model.payload;

import com.onezol.vertex.framework.common.model.payload.Payload;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.util.List;

@Data
@Schema(name = "UserSavePayload", description = "用户创建参数")
public class UserSavePayload implements Payload {

    @Schema(description = "用户ID")
    private Long id;

    @Schema(description = "用户名")
    @NotBlank(message = "用户名不能为空")
    @Size(min = 4, max = 20, message = "用户名长度必须在4-20位之间")
    private String username;

    @Schema(description = "用户姓名")
    private String name;

    @Schema(description = "用户昵称")
    @NotBlank(message = "用户昵称不能为空")
    @Size(min = 1, max = 20, message = "用户昵称长度必须在1-20位之间")
    private String nickname;

    @Schema(description = "密码")
    @NotBlank(message = "密码不能为空")
    @Size(min = 6, max = 25, message = "密码长度必须在6-25位之间")
    private String password;

    @Schema(description = "手机号码")
    private String phone;

    @Schema(description = "电子邮箱")
    private String email;

    @Schema(description = "性别")
    private Integer gender;

    @Schema(description = "部门ID")
    private Long departmentId;

    @Schema(description = "角色Code列表")
    private List<String> roleCodes;

    @Schema(description = "备注")
    private String remark;

    @Schema(description = "账户启用状态")
    private Integer status;

}
