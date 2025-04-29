package com.onezol.vertx.framework.common.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DictionaryEntry {

    private String label;

    private Object value;

    private String color;

    private Boolean disabled;

    private Object extra;

    public DictionaryEntry(String label, Object value) {
        this.setLabel(label);
        this.setValue(value);
        this.setDisabled(Boolean.FALSE);
    }

    public static DictionaryEntry of(String label, Object value) {
        return new DictionaryEntry(label, value);
    }

}
