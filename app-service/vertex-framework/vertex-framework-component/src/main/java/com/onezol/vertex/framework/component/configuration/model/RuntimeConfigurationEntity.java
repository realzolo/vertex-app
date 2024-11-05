package com.onezol.vertex.framework.component.configuration.model;

import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.onezol.vertex.framework.common.model.entity.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Schema(name = "RuntimeConfigurationEntity", description = "运行时配置")
@Data
@EqualsAndHashCode(callSuper = true)
@TableName(value = "vx_runtime_configuration")
public class RuntimeConfigurationEntity extends BaseEntity {

    @Schema(description = "所属主题")
    @TableField(value = "subject")
    private String subject;

    @Schema(description = "配置名称")
    @TableField(value = "name")
    private String name;

    @Schema(description = "配置编码")
    @TableField(value = "code")
    private String code;

    @Schema(description = "配置值")
    @TableField(value = "default_value")
    private String defaultValue;

    @Schema(description = "配置值")
    @TableField(value = "value", updateStrategy = FieldStrategy.ALWAYS)
    private String value;

    @Schema(description = "配置描述")
    @TableField(value = "description")
    private String description;

}
