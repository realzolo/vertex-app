package com.onezol.vertex.framework.component.dictionary.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
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

    @TableField("description")
    private Integer description;

    @TableField( "`group`")
    private String group;

    @TableField("color")
    private String color;

    @TableField("type")
    private String type;

}
