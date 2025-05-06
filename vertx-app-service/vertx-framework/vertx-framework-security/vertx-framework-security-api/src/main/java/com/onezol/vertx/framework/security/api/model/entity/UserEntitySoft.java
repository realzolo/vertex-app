package com.onezol.vertx.framework.security.api.model.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.onezol.vertx.framework.common.constant.enumeration.AccountStatus;
import com.onezol.vertx.framework.common.constant.enumeration.Gender;
import com.onezol.vertx.framework.common.skeleton.model.SoftDeletableEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;

@Schema(name = "用户实体")
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("app_user")
public class UserEntitySoft extends SoftDeletableEntity {

    @Schema(name = "用户名")
    @TableField("username")
    private String username;

    @Schema(name = "密码")
    @TableField("password")
    private String password;

    @Schema(name = "用户昵称")
    @TableField("nickname")
    private String nickname;

    @Schema(name = "用户姓名")
    @TableField("name")
    private String name;

    @Schema(name = "用户描述")
    @TableField("description")
    private String description;

    @Schema(name = "用户头像")
    @TableField("avatar")
    private String avatar;

    @Schema(name = "性别")
    @TableField("gender")
    private Gender gender;

    @Schema(name = "生日")
    @TableField("birthday")
    private LocalDate birthday;

    @Schema(name = "电话号码")
    @TableField("phone")
    private String phone;

    @Schema(name = "电子邮箱")
    @TableField("email")
    private String email;

    @Schema(name = "密码过期时间")
    @TableField("pwd_exp_date")
    private LocalDate pwdExpDate;

    @Schema(name = "账号状态")
    @TableField("status")
    private AccountStatus status;

}
