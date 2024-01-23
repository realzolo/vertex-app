package com.onezol.vertex.framework.security.api.model.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.onezol.vertex.framework.common.model.entity.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("vx_agency")
@Schema(name = "Agency", description = "$!{table.comment}")
public class AgencyEntity extends BaseEntity {

    @Schema(description = "组织编码")
    @TableField("code")
    private String code;

    @Schema(description = "组织名称")
    @TableField("name")
    private String name;

    @Schema(description = "父级编码")
    @TableField("parent_code")
    private String parentCode;

    @Schema(description = "组织简介")
    @TableField("description")
    private String description;

    @Schema(description = "组织级别")
    @TableField("level")
    private Integer level;

    @Schema(description = "排序号")
    @TableField("sort")
    private Integer sort;

    @Schema(description = "组织状态")
    @TableField("status")
    private Integer status;

}
