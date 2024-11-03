package com.onezol.vertex.framework.component.dictionary.model;

import com.onezol.vertex.framework.common.model.vo.VO;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class DictionaryItem implements VO {

    private Long id;

    private String name;

    private String value;

    private String color;

    private Long groupId;

    private String group;

    private Integer sort;

    private Integer status;

    private Boolean isBuiltIn;

    private String remark;

    private LocalDateTime createTime;

}
