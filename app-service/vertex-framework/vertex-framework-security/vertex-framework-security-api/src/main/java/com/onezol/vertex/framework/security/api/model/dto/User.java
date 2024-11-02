package com.onezol.vertex.framework.security.api.model.dto;

import com.onezol.vertex.framework.common.model.LabelValue;
import com.onezol.vertex.framework.common.model.dto.BaseDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;
import java.util.Collections;
import java.util.Dictionary;
import java.util.List;
import java.util.Set;

@Schema(description = "用户信息")
@Data
@EqualsAndHashCode(callSuper = true)
public class User extends BaseDTO {

    @Schema(description = "用户名")
    private String username;

    @Schema(description = "用户昵称")
    private String nickname;

    @Schema(description = "用户简介")
    private String introduction;

    @Schema(description = "用户头像")
    private String avatar;

    @Schema(description = "性别")
    private Integer gender;

    @Schema(description = "生日")
    private LocalDate birthday;

    @Schema(description = "电话号码")
    private String phone;

    @Schema(description = "电子邮箱")
    private String email;

    @Schema(description = "角色列表")
    private List<LabelValue<String, String>> roles = Collections.emptyList();

    @Schema(description = "权限列表")
    private List<String> permissions = Collections.emptyList();

    @Schema(description = "账号状态")
    private Integer status;

}
