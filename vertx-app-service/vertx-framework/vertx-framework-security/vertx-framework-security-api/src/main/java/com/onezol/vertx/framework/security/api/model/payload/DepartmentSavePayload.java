package com.onezol.vertx.framework.security.api.model.payload;

import com.onezol.vertx.framework.common.skeleton.model.BaseDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Schema(name = "部门参数")
@Data
@EqualsAndHashCode(callSuper = true)
public class DepartmentSavePayload extends BaseDTO {

    @Schema(name = "部门名称")
    private String name;

    @Schema(name = "父级ID")
    private Long parentId;

    @Schema(name = "祖级列表")
    private String ancestors;

    @Schema(name = "备注")
    private String remark;

    @Schema(name = "排序号")
    private Integer sort;

    @Schema(name = "角色状态")
    private Integer status;

}
