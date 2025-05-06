package com.onezol.vertx.framework.security.api.model.dto;

import com.onezol.vertx.framework.common.model.DataPairRecord;
import com.onezol.vertx.framework.common.skeleton.model.BaseDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

@Schema(name = "用户信息")
@Data
@EqualsAndHashCode(callSuper = true)
public class User extends BaseDTO {

    @Schema(name = "用户名")
    private String username;

    @Schema(name = "用户昵称")
    private String nickname;

    @Schema(name = "用户描述")
    private String description;

    @Schema(name = "用户头像")
    private String avatar;

    @Schema(name = "性别")
    private Integer gender;

    @Schema(name = "生日")
    private LocalDate birthday;

    @Schema(name = "电话号码")
    private String phone;

    @Schema(name = "电子邮箱")
    private String email;

    @Schema(name = "部门")
    private DataPairRecord department;

    @Schema(name = "角色列表")
    private List<DataPairRecord> roles = Collections.emptyList();

    @Schema(name = "权限列表")
    private List<String> permissions = Collections.emptyList();

    @Schema(name = "账号状态")
    private Integer status;

}
