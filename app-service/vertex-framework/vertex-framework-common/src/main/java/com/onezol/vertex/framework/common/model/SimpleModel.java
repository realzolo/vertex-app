package com.onezol.vertex.framework.common.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * Label Value 键值对
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SimpleModel<T> implements Serializable {

    private Long id;

    private String name;

    private String code;

    private T value;

    private String description;

    public static <T> SimpleModel<T> of(Long id, String name, String code, T value, String description) {
        SimpleModel<T> simpleModel = new SimpleModel<>();
        simpleModel.setId(id);
        simpleModel.setName(name);
        simpleModel.setCode(code);
        simpleModel.setValue(value);
        simpleModel.setDescription(description);
        return simpleModel;
    }

}
