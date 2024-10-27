package com.onezol.vertex.framework.security.api.model.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.onezol.vertex.framework.common.model.entity.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("vx_permission")
@Schema(name = "PermissionEntity", description = "$!{table.comment}")
public class PermissionEntity extends BaseEntity {

    @Schema(description = "权限名称")
    @TableField("name")
    private Long name;

    @Schema(description = "权限Code")
    @TableField("code")
    private String code;

    @Schema(description = "排序号")
    @TableField("sort")
    private Integer sort;

}
