package com.onezol.vertex.framework.security.api.model.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.onezol.vertex.framework.common.model.entity.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Schema(name = "用户角色关联实体")
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("vx_user_role")
public class UserRoleEntity extends BaseEntity {

    @Schema(name = "用户ID")
    @TableField("user_id")
    private Long userId;

    @Schema(name = "角色ID")
    @TableField("role_id")
    private Long roleId;

}
