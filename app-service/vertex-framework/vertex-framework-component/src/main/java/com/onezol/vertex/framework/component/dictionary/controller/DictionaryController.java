package com.onezol.vertex.framework.component.dictionary.controller;

import com.onezol.vertex.framework.common.constant.CacheKey;
import com.onezol.vertex.framework.common.helper.ResponseHelper;
import com.onezol.vertex.framework.common.model.GenericResponse;
import com.onezol.vertex.framework.common.model.LabelValue;
import com.onezol.vertex.framework.component.dictionary.service.DictionaryHelper;
import com.onezol.vertex.framework.component.dictionary.service.DictionaryService;
import com.onezol.vertex.framework.security.api.annotation.RestrictAccess;
import com.onezol.vertex.framework.support.cache.RedisCache;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@Tag(name = "数据字典")
@RestController
//@RestrictAccess
@RequestMapping("/dictionary")
public class DictionaryController {

    private final RedisCache redisCache;
    private final DictionaryService dictionaryService;

    public DictionaryController(RedisCache redisCache, DictionaryService dictionaryService) {
        this.redisCache = redisCache;
        this.dictionaryService = dictionaryService;
    }

    @GetMapping
    @Operation(summary = "获取字典列表")
    public GenericResponse<Map<String, Object>> getDictionaryMap() {
        Map<String, Object> cacheMap = redisCache.getCacheMap(CacheKey.DICTIONARY);
        return ResponseHelper.buildSuccessfulResponse(cacheMap);
    }

    @GetMapping("/{key}")
    @Operation(summary = "获取字典")
    public GenericResponse<List<LabelValue<String, String>>> getDictionary(@PathVariable("key") String key) {
        List<LabelValue<String, String>> dictionary = DictionaryHelper.get(key);
        return ResponseHelper.buildSuccessfulResponse(dictionary);
    }
}
