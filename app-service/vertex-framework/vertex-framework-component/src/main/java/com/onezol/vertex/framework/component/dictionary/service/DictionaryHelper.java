package com.onezol.vertex.framework.component.dictionary.service;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.onezol.vertex.framework.common.constant.CacheKey;
import com.onezol.vertex.framework.common.model.LabelValue;
import com.onezol.vertex.framework.common.util.SpringUtils;
import com.onezol.vertex.framework.common.util.StringUtils;
import com.onezol.vertex.framework.component.dictionary.model.SimpleDictionary;
import com.onezol.vertex.framework.support.cache.RedisCache;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Slf4j
public class DictionaryHelper {

    private static final RedisCache redisCache;

    static {
        redisCache = SpringUtils.getBean(RedisCache.class);
    }

    public static List<SimpleDictionary> get(String code) {
        if (StringUtils.isBlank(code)) {
            return Collections.emptyList();
        }
        Object cacheMapValue = redisCache.getCacheMapValue(CacheKey.DICTIONARY, code);
        if (Objects.isNull(cacheMapValue)) {
            return Collections.emptyList();
        }
        JSONArray objects = JSONArray.parse(cacheMapValue.toString());
        List<SimpleDictionary> dictionaries = new ArrayList<>();
        for (Object object : objects) {
            if (object instanceof JSONObject jsonObject) {
                SimpleDictionary dictionary = new SimpleDictionary();
                dictionary.setLabel(jsonObject.getString("label"));
                dictionary.setValue(jsonObject.getString("value"));
                dictionary.setColor(jsonObject.getString("color"));
                dictionary.setDisabled(jsonObject.getBoolean("disabled"));
                dictionaries.add(dictionary);
            } else {
                log.error("非法数据类型，无法解析为字典值");
            }
        }
        return dictionaries;
    }

}
