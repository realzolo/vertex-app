package com.onezol.vertex.framework.security.api.model.payload;

import com.onezol.vertex.framework.common.model.payload.Payload;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;


@Data
@Schema(name = "用户查询参数", description = "用户查询参数")
public class UserQueryPayload implements Payload {

    @Schema(description = "用户ID")
    private Long id;

    @Schema(description = "用户名")
    private String username;

    @Schema(description = "用户昵称")
    private String nickname;

    @Schema(description = "用户姓名")
    private String name;

    @Schema(description = "性别")
    private Integer gender;

    @Schema(description = "电话号码")
    private String phone;

    @Schema(description = "电子邮箱")
    private String email;

    @Schema(description = "账号状态")
    private Integer status;

}
