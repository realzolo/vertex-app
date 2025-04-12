package com.onezol.vertex.framework.security.api.model.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class SimpleDepartment implements Serializable {

    private Long id;

    private String name;

    public static SimpleDepartment of(Long id, String name) {
        SimpleDepartment simpleDepartment = new SimpleDepartment();
        simpleDepartment.setId(id);
        simpleDepartment.setName(name);
        return simpleDepartment;
    }

}
