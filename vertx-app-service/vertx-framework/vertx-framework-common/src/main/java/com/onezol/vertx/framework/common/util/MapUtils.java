package com.onezol.vertx.framework.common.util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * Map 工具类
 */
public final class MapUtils {

    private MapUtils() {
        throw new IllegalStateException("Utility class cannot be instantiated");
    }

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
     * 将List转换为Map
     *
     * @param list      源列表
     * @param keyMapper 生成key的函数
     * @param <K>       key类型
     * @param <V>       value类型
     * @return 转换后的Map
     */
    public static <K, V> Map<K, V> list2Map(List<V> list, Function<V, K> keyMapper) {
        return list2Map(list, keyMapper, Function.identity());
    }

    /**
     * 将List转换为Map
     *
     * @param list        源列表
     * @param keyMapper   生成key的函数
     * @param valueMapper 生成value的函数
     * @param <K>         key类型
     * @param <V>         value类型
     * @param <U>         最终Map的value类型
     * @return 转换后的Map
     */
    public static <K, V, U> Map<K, U> list2Map(
            List<V> list,
            Function<V, K> keyMapper,
            Function<V, U> valueMapper
    ) {
        if (list == null) {
            return new HashMap<>();
        }

        Map<K, U> map = new HashMap<>(list.size());
        for (V item : list) {
            if (item != null) {
                K key = keyMapper.apply(item);
                U value = valueMapper.apply(item);
                map.put(key, value);
            }
        }
        return map;
    }

}
