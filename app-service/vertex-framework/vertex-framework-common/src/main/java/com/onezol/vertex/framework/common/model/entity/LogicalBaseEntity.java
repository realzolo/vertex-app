package com.onezol.vertex.framework.common.model.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 基于MyBatis-Plus的基础实体类(逻辑删除)
 */
@Data
@EqualsAndHashCode(callSuper = true)
public abstract class LogicalBaseEntity extends BaseEntity {

    @Schema(description = "逻辑删除标志")
    @TableLogic
    @TableField("deleted")
    private boolean deleted = false;

}
