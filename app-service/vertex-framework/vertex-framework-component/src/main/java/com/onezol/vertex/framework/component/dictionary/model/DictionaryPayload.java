package com.onezol.vertex.framework.component.dictionary.model;

import lombok.Data;

@Data
public class DictionaryPayload {
    private String name;

    private String value;

    private Integer description;

    private String group;

    private String color;

    private String type;
}
