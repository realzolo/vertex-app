package com.onezol.vertex.framework.security.api.model.payload;

import com.onezol.vertex.framework.common.model.dto.BaseDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@Schema(name = "DepartmentSavePayload", description = "部门参数")
public class DepartmentSavePayload extends BaseDTO {

    @Schema(description = "部门名称")
    private String name;

    @Schema(description = "父级ID")
    private Long parentId;

    @Schema(description = "祖级列表")
    private String ancestors;

    @Schema(description = "备注")
    private String remark;

    @Schema(description = "排序号")
    private Integer sort;

    @Schema(description = "角色状态")
    private Integer status;

}
