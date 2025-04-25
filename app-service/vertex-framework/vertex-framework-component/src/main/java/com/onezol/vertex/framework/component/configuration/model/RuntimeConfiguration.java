package com.onezol.vertex.framework.component.configuration.model;

import com.onezol.vertex.framework.common.model.dto.BaseDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class RuntimeConfiguration extends BaseDTO {

    @Schema(name = "所属主题")
    private String subject;

    @Schema(name = "配置名称")
    private String name;

    @Schema(name = "配置编码")
    private String code;

    @Schema(name = "配置值")
    private String value;

    @Schema(name = "配置描述")
    private String description;

}
