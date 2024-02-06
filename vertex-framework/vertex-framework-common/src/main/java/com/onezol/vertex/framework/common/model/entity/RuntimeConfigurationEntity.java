package com.onezol.vertex.framework.common.model.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Schema(name = "RuntimeConfigurationEntity", description = "运行时配置")
@Data
@Builder
@EqualsAndHashCode(callSuper = true)
@TableName(value = "vx_runtime_configuration")
public class RuntimeConfigurationEntity extends BaseEntity {

    @Schema(description = "所属主题")
    @TableField(value = "subject")
    private String subject;

    @Schema(description = "配置名称")
    @TableField(value = "config_name")
    private String configName;

    @Schema(description = "配置键")
    @TableField(value = "config_key")
    private String configKey;

    @Schema(description = "配置值")
    @TableField(value = "config_value")
    private String configValue;

    @Schema(description = "配置描述")
    @TableField(value = "config_description")
    private String configDescription;

}
