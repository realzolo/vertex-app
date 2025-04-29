package com.onezol.vertx.framework.common.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * Key Value 键值对
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class KeyValuePair<K, V> implements Serializable {

    private K key;

    private V value;

}
