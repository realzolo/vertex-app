package com.onezol.vertex.framework.component.configuration.model;

import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.onezol.vertex.framework.common.model.entity.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Schema(name = "运行时配置实体", description = "程序运行时配置的配置项")
@Data
@EqualsAndHashCode(callSuper = true)
@TableName(value = "vx_runtime_configuration")
public class RuntimeConfigurationEntity extends BaseEntity {

    @Schema(name = "所属主题")
    @TableField(value = "subject")
    private String subject;

    @Schema(name = "配置名称")
    @TableField(value = "name")
    private String name;

    @Schema(name = "配置编码")
    @TableField(value = "code")
    private String code;

    @Schema(name = "配置值")
    @TableField(value = "default_value")
    private String defaultValue;

    @Schema(name = "配置值")
    @TableField(value = "value", updateStrategy = FieldStrategy.ALWAYS)
    private String value;

    @Schema(name = "配置描述")
    @TableField(value = "description")
    private String description;

}
