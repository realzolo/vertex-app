package com.onezol.vertex.framework.component.dictionary.controller;

import com.onezol.vertex.framework.common.constant.CacheKey;
import com.onezol.vertex.framework.support.support.ResponseHelper;
import com.onezol.vertex.framework.common.model.GenericResponse;
import com.onezol.vertex.framework.common.util.BeanUtils;
import com.onezol.vertex.framework.component.dictionary.model.DictionaryEntity;
import com.onezol.vertex.framework.component.dictionary.model.DictionaryItem;
import com.onezol.vertex.framework.component.dictionary.service.DictionaryService;
import com.onezol.vertex.framework.support.cache.RedisCache;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

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

//    @GetMapping("/{code}")
//    @Operation(summary = "获取字典")
//    public GenericResponse<List<LabelValue<String, String>>> getDictionaryByCode(@PathVariable("code") String code) {
//        List<LabelValue<String, String>> dictionary = DictionaryHelper.get(code);
//        return ResponseHelper.buildSuccessfulResponse(dictionary);
//    }

    @GetMapping("/{id}")
    @Operation(summary = "获取字典")
    public GenericResponse<DictionaryItem> getDictionaryById(@PathVariable("id") Long id) {
        DictionaryEntity dictionary = dictionaryService.getById(id);
        DictionaryItem bean = BeanUtils.toBean(dictionary, DictionaryItem.class);
        return ResponseHelper.buildSuccessfulResponse(bean);
    }

    @PostMapping
    @Operation(summary = "新增字典")
    public GenericResponse<Void> addDictionary(@RequestBody DictionaryItem dictionaryItem) {
        dictionaryService.createDictionary(dictionaryItem);
        return ResponseHelper.buildSuccessfulResponse();
    }

    @PutMapping("/{id}")
    @Operation(summary = "更新字典")
    public GenericResponse<Void> updateDictionary(@RequestBody DictionaryItem dictionaryItem) {
        DictionaryEntity entity = BeanUtils.toBean(dictionaryItem, DictionaryEntity.class);
        dictionaryService.updateById(entity);
        return ResponseHelper.buildSuccessfulResponse();
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除字典")
    public GenericResponse<Void> deleteDictionary(@PathVariable("id") Long id) {
        boolean ok = dictionaryService.removeById(id);
        if (!ok) {
            return ResponseHelper.buildFailedResponse("删除字典失败");
        }
        return ResponseHelper.buildSuccessfulResponse();
    }

    @GetMapping("/groups")
    @Operation(summary = "获取字典分组")
    public GenericResponse<List<DictionaryItem>> getDictionaryGroups() {
        List<DictionaryItem> groups = dictionaryService.listGroups();
        return ResponseHelper.buildSuccessfulResponse(groups);
    }

    @GetMapping("/items")
    @Operation(summary = "获取字典分组下的字典项")
    public GenericResponse<List<DictionaryItem>> getDictionaryItems(@RequestParam("groupId") Long groupId) {
        List<DictionaryItem> groups = dictionaryService.listItems(groupId);
        return ResponseHelper.buildSuccessfulResponse(groups);
    }
}
