package com.onezol.vertx.framework.common.skeleton.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Schema(name = "基础实体类(逻辑删除)", description = "基于MyBatis-Plus的基础实体类(逻辑删除)")
@Data
@EqualsAndHashCode(callSuper = true)
public abstract class SoftDeletableEntity extends BaseEntity {

    @Schema(name = "逻辑删除标志")
    @TableLogic
    @TableField("deleted")
    private boolean deleted = false;

}
