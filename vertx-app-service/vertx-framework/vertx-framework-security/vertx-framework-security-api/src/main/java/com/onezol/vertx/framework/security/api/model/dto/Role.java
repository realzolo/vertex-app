package com.onezol.vertx.framework.security.api.model.dto;

import com.onezol.vertx.framework.common.model.dto.BaseDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Set;

@Schema(name = "角色")
@Data
@EqualsAndHashCode(callSuper = true)
public class Role extends BaseDTO {

    @Schema(name = "角色名称")
    private String name;

    @Schema(name = "角色Code")
    private String code;

    @Schema(name = "角色列表")
    private Set<Long> permissionIds;

    @Schema(name = "排序号")
    private Integer sort;

    @Schema(name = "备注")
    private String remark;

    @Schema(name = "角色状态(0: 正常, 1: 禁用)")
    private Integer status;

}
