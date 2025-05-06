package com.onezol.vertx.framework.component.dictionary.service;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.onezol.vertx.framework.common.constant.CacheKey;
import com.onezol.vertx.framework.common.constant.enumeration.DictionaryType;
import com.onezol.vertx.framework.common.exception.InvalidParameterException;
import com.onezol.vertx.framework.common.model.DictionaryEntry;
import com.onezol.vertx.framework.common.skeleton.service.BaseServiceImpl;
import com.onezol.vertx.framework.common.util.BeanUtils;
import com.onezol.vertx.framework.component.dictionary.mapper.DictionaryMapper;
import com.onezol.vertx.framework.component.dictionary.model.Dictionary;
import com.onezol.vertx.framework.component.dictionary.model.DictionaryEntity;
import com.onezol.vertx.framework.support.cache.RedisCache;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class DictionaryService extends BaseServiceImpl<DictionaryMapper, DictionaryEntity> {

    private final RedisCache redisCache;

    public DictionaryService(RedisCache redisCache) {
        this.redisCache = redisCache;
    }

    public List<Dictionary> listGroups() {
        List<DictionaryEntity> entities = this.list(
                Wrappers.<DictionaryEntity>lambdaQuery()
                        .isNull(DictionaryEntity::getGroup)
                        .orderByDesc(DictionaryEntity::getType)
        );

        List<Dictionary> groups = new ArrayList<>(entities.size());
        for (DictionaryEntity entity : entities) {
            Dictionary group = new Dictionary();
            group.setId(entity.getId());
            group.setName(entity.getName());
            group.setValue(entity.getValue());
            group.setType(entity.getType().getValue());
            group.setBuiltin(entity.getBuiltin());
            groups.add(group);
        }
        return groups;
    }

    public List<Dictionary> listItems(Long groupId) {
        if (Objects.isNull(groupId)) {
            return Collections.emptyList();
        }
        DictionaryEntity group = this.getById(groupId);
        if (Objects.isNull(group)) {
            return Collections.emptyList();
        }
        List<DictionaryEntity> entities = this.list(
                Wrappers.<DictionaryEntity>lambdaQuery()
                        .eq(DictionaryEntity::getGroup, group.getValue())
                        .orderByDesc(DictionaryEntity::getCreateTime)
        );
        List<Dictionary> items = new ArrayList<>(entities.size());
        for (DictionaryEntity entity : entities) {
            Dictionary item = BeanUtils.toBean(entity, Dictionary.class);
            item.setType(entity.getType().getValue());
            items.add(item);
        }
        return items;
    }

    public void createDictionary(Dictionary dictionary) {
        DictionaryEntity entity;
        // 添加字典分组
        if (Objects.isNull(dictionary.getGroupId())) {
            long count = this.count(
                    Wrappers.<DictionaryEntity>lambdaQuery()
                            .eq(DictionaryEntity::getName, dictionary.getName())
                            .or()
                            .eq(DictionaryEntity::getValue, dictionary.getValue())
            );
            if (count > 0) {
                throw new InvalidParameterException("字典已存在");
            }
            entity = BeanUtils.toBean(dictionary, DictionaryEntity.class);
            entity.setGroup(null);
        }
        // 添加字典项
        else {
            long count = this.count(
                    Wrappers.<DictionaryEntity>lambdaQuery()
                            .eq(DictionaryEntity::getGroup, dictionary.getGroup())
                            .or(
                                    queryWrapper -> queryWrapper
                                            .eq(DictionaryEntity::getName, dictionary.getName())
                                            .or()
                                            .eq(DictionaryEntity::getValue, dictionary.getValue())
                            )
            );
            DictionaryEntity group = this.getById(dictionary.getGroupId());
            if (count > 0) {
                throw new InvalidParameterException("字典项已存在");
            }
            entity = BeanUtils.toBean(dictionary, DictionaryEntity.class);
            entity.setGroup(group.getValue());
        }
        entity.setType(DictionaryType.DICT);
        boolean ok = this.save(entity);
        if (!ok) {
            throw new InvalidParameterException("添加字典失败");
        }
    }

    public void syncDictionaryToRedis() {
        redisCache.deleteObject(CacheKey.DICTIONARY);

        List<DictionaryEntity> entities = this.list();
        Map<String, List<DictionaryEntry>> dictionaryMap = new HashMap<>();
        for (DictionaryEntity entity : entities) {
            if (Objects.isNull(entity.getGroup())) {
                dictionaryMap.put(entity.getValue(), new ArrayList<>());
                continue;
            }
            DictionaryEntry entry = new DictionaryEntry();
            entry.setLabel(entity.getName());
            entry.setValue(entity.getValue());
            entry.setColor(entity.getColor());
            entry.setDisabled(entity.getStatus().getValue() == 0);
            dictionaryMap.computeIfAbsent(entity.getGroup(), k -> new ArrayList<>()).add(entry);
        }

        redisCache.setCacheMap(CacheKey.DICTIONARY, dictionaryMap);
    }
}
