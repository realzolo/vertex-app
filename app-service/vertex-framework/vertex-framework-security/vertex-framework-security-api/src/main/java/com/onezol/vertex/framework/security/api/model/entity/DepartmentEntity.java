package com.onezol.vertex.framework.security.api.model.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.onezol.vertex.framework.common.constant.enumeration.DisEnableStatus;
import com.onezol.vertex.framework.common.model.entity.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Schema(name = "部门实体")
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("vx_department")
public class DepartmentEntity extends BaseEntity {

    @Schema(name = "部门名称")
    @TableField("name")
    private String name;

    @Schema(name = "父级ID")
    @TableField("parent_id")
    private Long parentId;

    @Schema(name = "祖级列表")
    @TableField("ancestors")
    private String ancestors;

    @Schema(name = "是否内置")
    @TableField("builtin")
    private Boolean builtIn;

    @Schema(name = "备注")
    @TableField("remark")
    private String remark;

    @Schema(name = "排序号")
    @TableField("sort")
    private Integer sort;

    @Schema(name = "角色状态")
    @TableField("status")
    private DisEnableStatus status;

}
