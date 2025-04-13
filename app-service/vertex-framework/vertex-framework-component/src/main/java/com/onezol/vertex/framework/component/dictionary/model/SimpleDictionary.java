package com.onezol.vertex.framework.component.dictionary.model;

import com.onezol.vertex.framework.common.model.vo.VO;
import lombok.Data;

@Data
public class SimpleDictionary implements VO {

    private String label;

    private String value;

    private String color;

    private Boolean disabled;

}
