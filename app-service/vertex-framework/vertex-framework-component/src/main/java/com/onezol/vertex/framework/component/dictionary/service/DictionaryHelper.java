package com.onezol.vertex.framework.component.dictionary.service;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.onezol.vertex.framework.common.constant.CacheKey;
import com.onezol.vertex.framework.common.model.LabelValue;
import com.onezol.vertex.framework.common.util.SpringUtils;
import com.onezol.vertex.framework.common.util.StringUtils;
import com.onezol.vertex.framework.support.cache.RedisCache;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Slf4j
public class DictionaryHelper {

    private static RedisCache redisCache;

    public static List<LabelValue<String, String>> get(String key) {
        if (StringUtils.isBlank(key)) {
            return Collections.emptyList();
        }
        if (Objects.isNull(redisCache)) {
            redisCache = SpringUtils.getBean(RedisCache.class);
        }
        Object cacheMapValue = redisCache.getCacheMapValue(CacheKey.DICTIONARY, key);
        if (Objects.isNull(cacheMapValue)) {
            return Collections.emptyList();
        }
        JSONArray objects = JSONArray.parse(cacheMapValue.toString());
        List<LabelValue<String, String>> dictionaries = new ArrayList<>();
        for (Object object : objects) {
            if (object instanceof JSONObject jsonObject) {
                String label = jsonObject.getString("label");
                String value = jsonObject.getString("value");
                LabelValue<String, String> dictionary = new LabelValue<>(label, value);
                dictionaries.add(dictionary);
            } else {
                log.error("非法数据类型，无法解析为字典值");
            }
        }
        return dictionaries;
    }

}
