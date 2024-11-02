package com.onezol.vertex.framework.security.api.model.dto;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.onezol.vertex.framework.common.model.dto.BaseDTO;
import com.onezol.vertex.framework.common.model.entity.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Set;

@Data
@EqualsAndHashCode(callSuper = true)
public class Role extends BaseDTO {

    @Schema(description = "角色名称")
    @TableField("name")
    private String name;

    @Schema(description = "角色Code")
    @TableField("code")
    private String code;

    @Schema(description = "角色Code")
    @TableField("code")
    private Set<Long> permissionIds;

    @Schema(description = "排序号")
    @TableField("sort")
    private Integer sort;

    @Schema(description = "角色状态(0: 正常, 1: 禁用)")
    @TableField("status")
    private Integer status;

}
