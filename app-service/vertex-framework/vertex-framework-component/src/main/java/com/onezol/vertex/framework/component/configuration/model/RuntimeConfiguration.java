package com.onezol.vertex.framework.component.configuration.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.onezol.vertex.framework.common.model.dto.BaseDTO;
import com.onezol.vertex.framework.common.model.entity.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class RuntimeConfiguration extends BaseDTO {

    @Schema(description = "所属主题")
    private String subject;

    @Schema(description = "配置名称")
    private String name;

    @Schema(description = "配置编码")
    private String code;

    @Schema(description = "配置值")
    private String value;

    @Schema(description = "配置描述")
    private String description;

}
