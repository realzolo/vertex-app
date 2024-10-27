package com.onezol.vertex.framework.common.util;

import com.onezol.vertex.framework.common.model.KeyValue;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

/**
 * Map 工具类
 */
public class MapUtils {

    /**
     * 从哈希表查找到 key 对应的 value，然后进一步处理
     * 如果查找到的 value 为 null 时，不进行处理
     *
     * @param map      哈希表
     * @param key      key
     * @param consumer 进一步处理的逻辑
     */
    public static <K, V> void findAndThen(Map<K, V> map, K key, Consumer<V> consumer) {
        if (map == null || map.isEmpty()) {
            return;
        }
        V value = map.get(key);
        if (value == null) {
            return;
        }
        consumer.accept(value);
    }

    /**
     * 将 List<KeyValue> 转换成 Map
     *
     * @param keyValues 键值对数组
     * @return Map
     */
    public static <K, V> Map<K, V> list2Map(List<KeyValue<K, V>> keyValues) {
        Map<K, V> map = new HashMap<>();
        keyValues.forEach(keyValue -> map.put(keyValue.getKey(), keyValue.getValue()));
        return map;
    }

}
