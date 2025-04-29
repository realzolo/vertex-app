package com.onezol.vertx.framework.component.dictionary.model;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Dictionary {

    private Long id;

    private String name;

    private String value;

    private String color;

    private Long groupId;

    private String group;

    private Integer type;

    private Boolean builtin;

    private Integer sort;

    private Integer status;

    private String remark;

    private LocalDateTime createTime;

}
