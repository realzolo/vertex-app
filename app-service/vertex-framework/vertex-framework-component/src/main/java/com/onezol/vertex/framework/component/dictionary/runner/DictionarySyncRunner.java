package com.onezol.vertex.framework.component.dictionary.runner;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.onezol.vertex.framework.common.constant.CacheKey;
import com.onezol.vertex.framework.common.constant.enumeration.Enumeration;
import com.onezol.vertex.framework.common.model.LabelValue;
import com.onezol.vertex.framework.common.util.StringUtils;
import com.onezol.vertex.framework.component.dictionary.model.DictionaryEntity;
import com.onezol.vertex.framework.component.dictionary.service.DictionaryService;
import com.onezol.vertex.framework.support.cache.RedisCache;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.io.File;
import java.sql.Wrapper;
import java.util.*;

@Slf4j
@Component
public class DictionarySyncRunner implements ApplicationRunner {
    private static final List<String> ENUM_PACKAGE = Arrays.asList(
            "com.onezol.vertex.framework.common",
            "com.onezol.vertex.framework.security"
    );
    private final RedisCache redisCache;
    private final DictionaryService dictionaryService;

    public DictionarySyncRunner(RedisCache redisCache, DictionaryService dictionaryService) {
        this.redisCache = redisCache;
        this.dictionaryService = dictionaryService;
    }

    @Override
    public void run(ApplicationArguments args) {
        Map<String, List<LabelValue<String, String>>> enumMap = this.getEnumMap();
        Map<String, List<LabelValue<String, String>>> dictMap = this.getDictMap();
        int enumSize = enumMap.size();
        int dictSize = dictMap.size();
        dictMap.putAll(enumMap);

        JSONObject simpleJsonValue = this.toSimpleJsonValue(dictMap);

        redisCache.deleteObject(CacheKey.DICTIONARY);
        redisCache.setCacheMap(CacheKey.DICTIONARY, simpleJsonValue);

        this.syncEnumsToDictionaryTable(enumMap);

        log.info("字典数据已同步到Redis缓存中，其中枚举类共 {} 项，字典类共 {} 项", enumSize, dictSize);
    }

    /**
     * TODO: 获取字典类的Map
     */
    private Map<String, List<LabelValue<String, String>>> getDictMap() {
//        return dictionaryService.getDictionaryMap();
        return new HashMap<>();
    }

    /**
     * 获取枚举类的Map
     */
    private Map<String, List<LabelValue<String, String>>> getEnumMap() {
        // 获取EnumService的实现类
        List<Class<?>> implementationClasses = findInterfaceImplementations(Enumeration.class);

        Map<String, List<LabelValue<String, String>>> enumMap = new HashMap<>(implementationClasses.size());
        root:
        for (Class<?> clazz : implementationClasses) {
            if (!clazz.isEnum()) {
                continue;
            }
            // 获取枚举类的所有枚举值
            Object[] enumConstants = clazz.getEnumConstants();

            List<LabelValue<String, String>> options = new ArrayList<>();
            for (Object enumConstant : enumConstants) {
                // 获取枚举值的name和value
                Enumeration<?> aEnum = (Enumeration<?>) enumConstant;
                if (StringUtils.isBlank(aEnum.getName())) {
                    // 跳过非字典类型的枚举值
                    continue root;
                }
                LabelValue<String, String> option = new LabelValue<>(aEnum.getName(), aEnum.getValue().toString());
                options.add(option);
            }

            String key = StringUtils.camelCaseToUnderline(clazz.getSimpleName()).toLowerCase().replace("_enum", "");
            enumMap.put(key, options);
        }
        return enumMap;
    }

    public List<Class<?>> findInterfaceImplementations(Class<?> interfaceClass) {
        List<Class<?>> implementationClasses = new ArrayList<>();

        // 获取当前线程的类加载器
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();

        for (String packageName : ENUM_PACKAGE) {
            try {
                // 获取当前项目的根包目录
                String rootPath = Objects.requireNonNull(classLoader.getResource(packageName.replace(".", "/"))).getPath();
                File rootDir = new File(rootPath);

                // 递归扫描根包下的所有类文件
                scanDirectoryForClasses(rootDir, packageName, classLoader, interfaceClass, implementationClasses);
            } catch (Exception ignored) {
                log.error("无法扫描包：{}", packageName);
            }
        }

        return implementationClasses;
    }

    private void scanDirectoryForClasses(File directory, String packageName, ClassLoader classLoader,
                                         Class<?> interfaceClass, List<Class<?>> implementationClasses) {
        if (!directory.exists() || !directory.isDirectory()) {
            return;
        }

        File[] files = directory.listFiles();
        assert files != null;
        for (File file : files) {
            if (file.isDirectory()) {
                String newPackageName = packageName + "." + file.getName();
                scanDirectoryForClasses(file, newPackageName, classLoader, interfaceClass, implementationClasses);
            } else if (file.getName().endsWith(".class")) {
                String className = packageName + "." +
                        file.getName().substring(0, file.getName().length() - 6);

                try {
                    Class<?> clazz = classLoader.loadClass(className);
                    if (interfaceClass.isAssignableFrom(clazz)) {
                        implementationClasses.add(clazz);
                    }
                } catch (ClassNotFoundException e) {
                    log.error("无法加载类：{}", className, e);
                }
            }
        }
    }

    /**
     * LabelValue 转 JSON
     *
     * @param dictMap 字典数据
     */
    private JSONObject toSimpleJsonValue(Map<String, List<LabelValue<String, String>>> dictMap) {
        Set<Map.Entry<String, List<LabelValue<String, String>>>> entries = dictMap.entrySet();
        JSONObject jsonMap = new JSONObject();
        for (Map.Entry<String, List<LabelValue<String, String>>> entry : entries) {
            JSONArray jsonArray = new JSONArray();
            for (LabelValue<String, String> labelValue : entry.getValue()) {
                JSONObject jsonValue = new JSONObject();
                jsonValue.put("label", labelValue.getLabel());
                jsonValue.put("value", labelValue.getValue());
                jsonArray.add(jsonValue);
            }
            jsonMap.put(entry.getKey(), jsonArray.toJSONString());
        }
        return jsonMap;
    }


    /**
     * 同步枚举类到字典表
     * @param enumMap 枚举类
     */
    private void syncEnumsToDictionaryTable(Map<String, List<LabelValue<String, String>>> enumMap) {
        log.info("正在清除枚举类字典表数据");
        dictionaryService.remove(Wrappers.<DictionaryEntity>lambdaQuery().eq(DictionaryEntity::getType, "ENUM"));

        log.info("开始同步枚举类到字典表");
        Set<Map.Entry<String, List<LabelValue<String, String>>>> entries = enumMap.entrySet();
        List<DictionaryEntity> dictionaries = new ArrayList<>();
        for (Map.Entry<String, List<LabelValue<String, String>>> entry : entries) {

            for (LabelValue<String, String> labelValue : entry.getValue()) {
                DictionaryEntity dictionary = new DictionaryEntity();
                dictionary.setName(labelValue.getLabel());
                dictionary.setValue(labelValue.getValue());
                dictionary.setGroup(entry.getKey());
                dictionary.setType("ENUM");
                dictionaries.add(dictionary);
            }
        }
        dictionaryService.saveBatch(dictionaries);
        log.info("同步枚举类到字典表成功");
    }
}
