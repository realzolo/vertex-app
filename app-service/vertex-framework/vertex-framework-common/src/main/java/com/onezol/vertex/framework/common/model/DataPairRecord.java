package com.onezol.vertex.framework.common.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.io.Serializable;

@Data
public class DataPairRecord implements Serializable {

    private Long id;

    private String name;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String code;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Object value;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String description;

    public DataPairRecord() {
    }

    public DataPairRecord(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public DataPairRecord(Long id, String name, String code) {
        this.id = id;
        this.name = name;
        this.code = code;
    }

    public DataPairRecord(Long id, String name, String code, Object value) {
        this.id = id;
        this.name = name;
        this.code = code;
        this.value = value;
    }

    public DataPairRecord(Long id, String name, String code, Object value, String description) {
        this.id = id;
        this.name = name;
        this.code = code;
        this.value = value;
        this.description = description;
    }

}
