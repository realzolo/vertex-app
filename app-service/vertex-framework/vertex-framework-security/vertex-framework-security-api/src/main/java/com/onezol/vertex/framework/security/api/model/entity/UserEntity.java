package com.onezol.vertex.framework.security.api.model.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.onezol.vertex.framework.common.constant.enumeration.AccountStatusEnum;
import com.onezol.vertex.framework.common.constant.enumeration.GenderEnum;
import com.onezol.vertex.framework.common.model.entity.BaseEntity;
import com.onezol.vertex.framework.common.model.entity.LogicalBaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("vx_user")
@Schema(name = "UserEntity", description = "$!{table.comment}")
public class UserEntity extends LogicalBaseEntity {

    @Schema(description = "用户名")
    @TableField("username")
    private String username;

    @Schema(description = "密码")
    @TableField("password")
    private String password;

    @Schema(description = "用户昵称")
    @TableField("nickname")
    private String nickname;

    @Schema(description = "用户姓名")
    @TableField("name")
    private String name;

    @Schema(description = "用户简介")
    @TableField("introduction")
    private String introduction;

    @Schema(description = "用户头像")
    @TableField("avatar")
    private String avatar;

    @Schema(description = "性别")
    @TableField("gender")
    private GenderEnum gender;

    @Schema(description = "生日")
    @TableField("birthday")
    private LocalDate birthday;

    @Schema(description = "电话号码")
    @TableField("phone")
    private String phone;

    @Schema(description = "电子邮箱")
    @TableField("email")
    private String email;

    @Schema(description = "密码过期时间")
    @TableField("pwd_exp_date")
    private LocalDate pwdExpDate;

    @Schema(description = "账号状态")
    @TableField("status")
    private AccountStatusEnum status;

}
