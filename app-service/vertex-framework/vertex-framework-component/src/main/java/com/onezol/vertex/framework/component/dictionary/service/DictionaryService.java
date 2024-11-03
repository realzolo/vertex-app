package com.onezol.vertex.framework.component.dictionary.service;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.onezol.vertex.framework.common.exception.RuntimeBizException;
import com.onezol.vertex.framework.common.mvc.service.BaseServiceImpl;
import com.onezol.vertex.framework.common.util.BeanUtils;
import com.onezol.vertex.framework.common.util.StringUtils;
import com.onezol.vertex.framework.component.dictionary.mapper.DictionaryMapper;
import com.onezol.vertex.framework.component.dictionary.model.DictionaryEntity;
import com.onezol.vertex.framework.component.dictionary.model.DictionaryItem;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Service
public class DictionaryService extends BaseServiceImpl<DictionaryMapper, DictionaryEntity> {

    public List<DictionaryItem> listGroups() {
        List<DictionaryEntity> entities = this.list(
                Wrappers.<DictionaryEntity>lambdaQuery()
                        .isNull(DictionaryEntity::getGroup)
                        .orderByDesc(DictionaryEntity::getType)
        );

        List<DictionaryItem> groups = new ArrayList<>(entities.size());
        for (DictionaryEntity entity : entities) {
            DictionaryItem group = new DictionaryItem();
            group.setId(entity.getId());
            group.setName(entity.getName());
            group.setValue(entity.getValue());
            group.setIsBuiltIn(this.isBuiltIn(entity.getType()));
            groups.add(group);
        }
        return groups;
    }

    public List<DictionaryItem> listItems(Long groupId) {
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
        List<DictionaryItem> items = new ArrayList<>(entities.size());
        for (DictionaryEntity entity : entities) {
            DictionaryItem item = BeanUtils.toBean(entity, DictionaryItem.class);
            item.setIsBuiltIn(this.isBuiltIn(entity.getType()));
            items.add(item);
        }
        return items;
    }

    private boolean isBuiltIn(String type) {
        return Objects.equals("ENUM", type);
    }

    public void createDictionary(DictionaryItem dictionaryItem) {
        DictionaryEntity entity;
        // 添加字典分组
        if (Objects.isNull(dictionaryItem.getGroupId())) {
            long count = this.count(
                    Wrappers.<DictionaryEntity>lambdaQuery()
                            .eq(DictionaryEntity::getName, dictionaryItem.getName())
                            .or()
                            .eq(DictionaryEntity::getValue, dictionaryItem.getValue())
            );
            if (count > 0) {
                throw new RuntimeBizException("字典已存在");
            }
            entity = BeanUtils.toBean(dictionaryItem, DictionaryEntity.class);
            entity.setGroup(null);
        }
        // 添加字典项
        else {
            long count = this.count(
                    Wrappers.<DictionaryEntity>lambdaQuery()
                            .eq(DictionaryEntity::getGroup, dictionaryItem.getGroup())
                            .or(
                                    queryWrapper -> queryWrapper
                                            .eq(DictionaryEntity::getName, dictionaryItem.getName())
                                            .or()
                                            .eq(DictionaryEntity::getValue, dictionaryItem.getValue())
                            )
            );
            DictionaryEntity group = this.getById(dictionaryItem.getGroupId());
            if (count > 0) {
                throw new RuntimeBizException("字典项已存在");
            }
            entity = BeanUtils.toBean(dictionaryItem, DictionaryEntity.class);
            entity.setGroup(group.getValue());
        }
        entity.setType("DICT");
        boolean ok = this.save(entity);
        if (!ok) {
            throw new RuntimeBizException("添加字典失败");
        }
    }
}
