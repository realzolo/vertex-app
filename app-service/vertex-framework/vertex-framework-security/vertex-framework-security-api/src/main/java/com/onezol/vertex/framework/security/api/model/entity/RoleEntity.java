package com.onezol.vertex.framework.security.api.model.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.onezol.vertex.framework.common.model.entity.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Schema(name = "角色实体")
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("vx_role")
public class RoleEntity extends BaseEntity {

    @Schema(name = "角色名称")
    @TableField("name")
    private String name;

    @Schema(name = "角色Code")
    @TableField("code")
    private String code;

    @Schema(name = "排序号")
    @TableField("sort")
    private Integer sort;

    @Schema(name = "备注")
    @TableField("remark")
    private String remark;

    @Schema(name = "角色状态(0: 正常, 1: 禁用)")
    @TableField("status")
    private Integer status;

}
