package com.onezol.vertx.framework.security.api.model.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.onezol.vertx.framework.common.skeleton.model.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Schema(name = "角色部门关联实体")
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("app_role_permission")
public class RolePermissionEntity extends BaseEntity {

    @Schema(name = "角色ID")
    @TableField("role_id")
    private Long roleId;

    @Schema(name = "权限ID")
    @TableField("permission_id")
    private Long permissionId;

}
