package com.onezol.vertex.framework.security.api.model.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.onezol.vertex.framework.common.constant.enumeration.DisEnableStatusEnum;
import com.onezol.vertex.framework.common.model.entity.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("vx_department")
@Schema(name = "DepartmentEntity", description = "$!{table.comment}")
public class DepartmentEntity extends BaseEntity {

    @Schema(description = "部门名称")
    @TableField("name")
    private String name;

    @Schema(description = "父级ID")
    @TableField("parent_id")
    private Long parentId;

    @Schema(description = "祖级列表")
    @TableField("ancestors")
    private String ancestors;

    @Schema(description = "是否内置")
    @TableField("builtin")
    private Boolean builtIn;

    @Schema(description = "备注")
    @TableField("remark")
    private String remark;

    @Schema(description = "排序号")
    @TableField("sort")
    private Integer sort;

    @Schema(description = "角色状态")
    @TableField("status")
    private DisEnableStatusEnum status;

}
