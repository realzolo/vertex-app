package com.onezol.vertex.framework.common.util;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.alibaba.fastjson2.JSONWriter;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * JSON工具类，提供了JSON序列化和反序列化的方法，以及常用的JSON操作功能。
 */
@SuppressWarnings({"unchecked", "unused"})
public class JsonUtils {
    /**
     * 将对象转换为JSON字符串。
     *
     * @param object 要转换的对象
     * @return 对象的JSON表示形式
     */
    public static String toJsonString(Object object) {
        return JSON.toJSONString(object, JSONWriter.Feature.WriteMapNullValue);
    }

    /**
     * 将JSON字符串转换为指定类型的对象。
     *
     * @param json  JSON字符串
     * @param clazz 目标对象的类型
     * @param <T>   目标对象类型
     * @return 解析后的目标对象
     */
    public static <T> T parseObject(String json, Class<T> clazz) {
        return JSON.parseObject(json, clazz);
    }

    /**
     * 将JSON字符串转换为指定类型的对象。
     *
     * @param json JSON字符串
     * @param type 目标对象的类型
     * @param <T>  目标对象类型
     * @return 解析后的目标对象
     */
    public static <T> T parseObject(String json, Type type) {
        return JSON.parseObject(json, type);
    }

    /**
     * 将JSON字符串转换为Map对象。
     *
     * @param json JSON字符串
     * @return 解析后的Map对象
     */
    public static Map<String, Object> toMap(String json) {
        return JSON.parseObject(json, HashMap.class);
    }

    /**
     * 将JSON对象转换为Map对象。
     *
     * @param jsonObject JSON对象
     * @return Map对象
     */
    public static Map<String, Object> toMap(JSONObject jsonObject) {
        return jsonObject.toJavaObject(HashMap.class);
    }

    /**
     * 将Map对象转换为JSON字符串。
     *
     * @param map Map对象
     * @return JSON字符串
     */
    public static String mapToJsonString(Map<String, Object> map) {
        return JSON.toJSONString(map);
    }

    /**
     * 将JSON字符串解析为JSONObject对象。
     *
     * @param json JSON字符串
     * @return 解析后的JSONObject对象
     */
    public static JSONObject parseJsonObject(String json) {
        return JSON.parseObject(json);
    }

    /**
     * 将JSON字符串解析为JSONArray对象。
     *
     * @param json JSON字符串
     * @return 解析后的JSONArray对象
     */
    public static JSONArray parseJsonArray(String json) {
        return JSON.parseArray(json);
    }

    /**
     * 将Java对象列表转换为JSON数组字符串。
     *
     * @param list Java对象列表
     * @param <T>  对象类型
     * @return JSON数组字符串
     */
    public static <T> String listToJsonArray(List<T> list) {
        return JSON.toJSONString(list, JSONWriter.Feature.WriteMapNullValue);
    }

    /**
     * 将JSON数组字符串解析为Java对象列表。
     *
     * @param jsonArray JSON数组字符串
     * @param clazz     目标对象的类型
     * @param <T>       目标对象类型
     * @return 解析后的Java对象列表
     */
    public static <T> List<T> jsonArrayToList(String jsonArray, Class<T> clazz) {
        return JSON.parseArray(jsonArray, clazz);
    }
}
