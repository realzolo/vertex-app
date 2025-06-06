package com.onezol.vertx.framework.security.api.model.payload;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;


@Schema(name = "用户查询参数")
@Data
public class UserQueryPayload implements Serializable {

    @Schema(name = "用户ID")
    private Long id;

    @Schema(name = "用户名")
    private String username;

    @Schema(name = "用户昵称")
    private String nickname;

    @Schema(name = "用户描述")
    private String description;

    @Schema(name = "性别")
    private Integer gender;

    @Schema(name = "电话号码")
    private String phone;

    @Schema(name = "电子邮箱")
    private String email;

    @Schema(name = "账号状态")
    private Integer status;

    @Schema(name = "模糊关键字")
    private String keyword;

    @Schema(name = "部门ID")
    private Long departmentId;

    @Schema(name = "角色ID")
    private Long roleId;

    @Schema(name = "开始时间")
    private String startTime;

    @Schema(name = "结束时间")
    private String endTime;

}
