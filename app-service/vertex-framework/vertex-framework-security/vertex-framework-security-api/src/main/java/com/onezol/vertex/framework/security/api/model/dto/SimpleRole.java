package com.onezol.vertex.framework.security.api.model.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class SimpleRole implements Serializable {

    private Long id;

    private String name;

    private String code;

    public static SimpleRole of(Long id, String name, String code) {
        SimpleRole simpleRole = new SimpleRole();
        simpleRole.setId(id);
        simpleRole.setName(name);
        simpleRole.setCode(code);
        return simpleRole;
    }

}
