package com.onezol.vertx.framework.security.api.model.payload;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(name = "用户基础信息更新参数")
@Data
public class UserProfileBasicInfUpdatePayload {

    @Schema(name = "用户ID")
    private Long id;

    @Schema(name = "用户昵称")
    private String nickname;

    @Schema(name = "性别")
    private Integer gender;

}
