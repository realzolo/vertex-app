package com.onezol.vertex.framework.common.model.pojo;

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
public class LabelValue<L, V> implements Serializable {

    private L label;

    private V value;

}
