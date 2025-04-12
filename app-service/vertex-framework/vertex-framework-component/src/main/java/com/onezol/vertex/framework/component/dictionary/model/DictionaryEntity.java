package com.onezol.vertex.framework.component.dictionary.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.onezol.vertex.framework.common.constant.enumeration.DisEnableStatusEnum;
import com.onezol.vertex.framework.common.model.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("vx_dictionary")
public class DictionaryEntity extends BaseEntity {

    @TableField("name")
    private String name;

    @TableField("value")
    private String value;

    @TableField("color")
    private String color;

    @TableField("remark")
    private String remark;

    @TableField("`group`")
    private String group;

    @TableField("builtin")
    private Boolean builtIn;

    @TableField("sort")
    private Integer sort;

    @TableField("type")
    private String type;

    @TableField("status")
    private DisEnableStatusEnum status;

}
