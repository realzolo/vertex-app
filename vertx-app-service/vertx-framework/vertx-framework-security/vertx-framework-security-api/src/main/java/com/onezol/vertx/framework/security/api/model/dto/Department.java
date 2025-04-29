package com.onezol.vertx.framework.security.api.model.dto;

import com.onezol.vertx.framework.common.model.dto.BaseDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Schema(name = "部门")
@Data
@EqualsAndHashCode(callSuper = true)
public class Department extends BaseDTO {

    @Schema(name = "部门名称")
    private String name;

    @Schema(name = "父级ID")
    private Long parentId;

    @Schema(name = "祖级列表")
    private String ancestors;

    @Schema(name = "是否内置")
    private Boolean builtIn;

    @Schema(name = "备注")
    private String remark;

    @Schema(name = "排序号")
    private Integer sort;

    @Schema(name = "角色状态")
    private Integer status;

}
