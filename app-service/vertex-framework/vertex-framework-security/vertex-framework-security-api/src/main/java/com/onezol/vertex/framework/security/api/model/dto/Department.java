package com.onezol.vertex.framework.security.api.model.dto;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.onezol.vertex.framework.common.constant.enumeration.DisEnableStatusEnum;
import com.onezol.vertex.framework.common.model.dto.BaseDTO;
import com.onezol.vertex.framework.common.model.entity.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@Schema(name = "Department", description = "部门")
public class Department extends BaseDTO {

    @Schema(description = "部门名称")
    private String name;

    @Schema(description = "父级ID")
    private Long parentId;

    @Schema(description = "祖级列表")
    private String ancestors;

    @Schema(description = "是否内置")
    private Boolean builtIn;

    @Schema(description = "备注")
    private String remark;

    @Schema(description = "排序号")
    private Integer sort;

    @Schema(description = "角色状态")
    private Integer status;

}
