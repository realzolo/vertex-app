package com.onezol.vertex.framework.security.api.model.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.onezol.vertex.framework.common.model.entity.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Schema(name = "用户部门关联实体")
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("vx_user_department")
public class UserDepartmentEntity extends BaseEntity {

    @Schema(name = "用户ID")
    @TableField("user_id")
    private Long userId;

    @Schema(name = "部门ID")
    @TableField("dept_id")
    private Long departmentId;

}
