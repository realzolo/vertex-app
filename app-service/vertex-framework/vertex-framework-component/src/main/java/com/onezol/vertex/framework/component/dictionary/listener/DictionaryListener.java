package com.onezol.vertex.framework.component.dictionary.listener;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.onezol.vertex.framework.common.annotation.EnumDictionary;
import com.onezol.vertex.framework.common.constant.enumeration.DictionaryTypeEnum;
import com.onezol.vertex.framework.common.constant.enumeration.DisEnableStatusEnum;
import com.onezol.vertex.framework.common.constant.enumeration.Enumeration;
import com.onezol.vertex.framework.common.util.StringUtils;
import com.onezol.vertex.framework.component.dictionary.model.DictionaryEntity;
import com.onezol.vertex.framework.component.dictionary.service.DictionaryService;
import com.onezol.vertex.framework.support.event.ApplicationClassScanEvent;
import com.onezol.vertex.framework.support.manager.async.AsyncTaskManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.*;

@Slf4j
@Component
public class DictionaryListener {

    private final DictionaryService dictionaryService;

    public DictionaryListener(DictionaryService dictionaryService) {
        this.dictionaryService = dictionaryService;
    }

    @EventListener(ApplicationClassScanEvent.class)
    public void onApplicationClassScanEvent(ApplicationClassScanEvent event) {
        Class<?>[] classes = event.getEnums();
        AsyncTaskManager.getInstance().execute(() -> this.run(classes));
    }

    private void run(Class<?>[] classes) {
        List<DictionaryEntity> dictionaries = new ArrayList<>();
        Set<String> groupCodeSet = new HashSet<>();
        root:
        for (Class<?> clazz : classes) {
            EnumDictionary annotation = clazz.getAnnotation(EnumDictionary.class);
            if (Enumeration.class.isAssignableFrom(clazz) && clazz.isEnum() && Objects.nonNull(annotation)) {
                String groupName = annotation.name();
                String groupCode = annotation.value();
                if (groupCodeSet.contains(groupCode)) {
                    throw new RuntimeException("枚举类 " + clazz.getName() + " 的字典分组编码 " + groupCode + " 已存在，请修改枚举类上的注解");
                }

                if (!checkDictionaryCode(groupCode)) {
                    log.warn("枚举类 {} 的字典分组编码 {} 不符合规范, 建议修改! （建议：小写的下划线字符串，只能由字母、数字、下划线组成）", clazz.getName(), groupCode);
                }
                groupCode = StringUtils.camelCaseToUnderline(groupCode);
                this.createDictionaryGroup(dictionaries, groupName, groupCode);
                groupCodeSet.add(groupCode);

                // 获取枚举类的所有枚举值
                Object[] enumConstants = clazz.getEnumConstants();
                for (Object enumConstant : enumConstants) {
                    // 获取枚举值的name和value
                    Enumeration<?> aEnum = (Enumeration<?>) enumConstant;
                    // 跳过非字典类型的枚举值
                    if (Objects.isNull(aEnum.getName()) || Objects.isNull(aEnum.getValue())) {
                        continue root;
                    }
                    this.createDictionaryItem(dictionaries, groupCode, aEnum);
                }
            }
        }

        log.info("正在清除枚举类字典表数据");

        dictionaryService.remove(Wrappers.<DictionaryEntity>lambdaQuery().eq(DictionaryEntity::getType, DictionaryTypeEnum.ENUM.getValue()));

        log.info("开始同步枚举类到字典表");

        dictionaryService.saveBatch(dictionaries);

        log.info("同步枚举类到字典表完成");

        log.info("开始同步字典数据到Redis缓存");

        dictionaryService.syncDictionaryToRedis();

        log.info("同步字典数据到Redis缓存完成");
    }

    /**
     * 创建字典组
     */
    private void createDictionaryGroup(List<DictionaryEntity> dictionaries, String name, String value) {
        DictionaryEntity group = new DictionaryEntity();
        group.setName(name);
        group.setValue(value);
        group.setType(DictionaryTypeEnum.ENUM);
        group.setRemark("系统内置");
        group.setBuiltin(Boolean.TRUE);
        group.setStatus(DisEnableStatusEnum.ENABLE);
        dictionaries.add(group);
    }

    /**
     * 创建字典项
     */
    private void createDictionaryItem(List<DictionaryEntity> dictionaries, String groupCode, Enumeration<?> aEnum) {
        DictionaryEntity dictionaryItem = new DictionaryEntity();
        dictionaryItem.setName(aEnum.getName());
        dictionaryItem.setValue(String.valueOf(aEnum.getValue()));
        dictionaryItem.setColor(aEnum.getColor());
        dictionaryItem.setGroup(groupCode);
        dictionaryItem.setType(DictionaryTypeEnum.ENUM);
        dictionaryItem.setRemark("系统内置");
        dictionaryItem.setBuiltin(Boolean.TRUE);
        dictionaryItem.setStatus(DisEnableStatusEnum.ENABLE);
        dictionaries.add(dictionaryItem);
    }


    /**
     * 检查字典格式：必须是小写的下划线字符串，不能有特殊字符，只能是字母、数字、下划线，不能以数字开头，不能以下划线结尾
     */
    private boolean checkDictionaryCode(String code) {
        return code.matches("^[a-z][a-z0-9_]*[a-z0-9]$");
    }

}
