package com.onezol.vertex.framework.common.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DataPairRecord implements Serializable {

    private Long id;

    private String name;

    private String code;

    private Object value;

    private String description;

}
