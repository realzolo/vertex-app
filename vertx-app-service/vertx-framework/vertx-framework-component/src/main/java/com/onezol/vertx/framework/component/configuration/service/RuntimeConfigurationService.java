package com.onezol.vertx.framework.component.configuration.service;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.onezol.vertx.framework.common.exception.InvalidParameterException;
import com.onezol.vertx.framework.common.model.DataPairRecord;
import com.onezol.vertx.framework.common.model.DictionaryEntry;
import com.onezol.vertx.framework.common.skeleton.service.BaseServiceImpl;
import com.onezol.vertx.framework.common.util.BeanUtils;
import com.onezol.vertx.framework.component.configuration.mapper.RuntimeConfigurationMapper;
import com.onezol.vertx.framework.component.configuration.model.RuntimeConfiguration;
import com.onezol.vertx.framework.component.configuration.model.RuntimeConfigurationEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Properties;

@Service
public class RuntimeConfigurationService extends BaseServiceImpl<RuntimeConfigurationMapper, RuntimeConfigurationEntity> {

    /**
     * 获取配置列表
     *
     * @param subject 配置主题
     */
    public List<DictionaryEntry> listConfigurationToDictionaryEntry(String subject) {
        List<RuntimeConfigurationEntity> configurationEntities = this.list(
                Wrappers.<RuntimeConfigurationEntity>lambdaQuery()
                        .eq(RuntimeConfigurationEntity::getSubject, subject)
        );
        return configurationEntities.stream()
                .map(entity -> DictionaryEntry.of(entity.getCode(), entity.getValue()))
                .toList();
    }

    /**
     * 获取配置列表
     *
     * @param subject 配置主题
     */
    public List<DataPairRecord> listConfigurations(String subject) {
        List<RuntimeConfigurationEntity> entities = this.list(
                Wrappers.<RuntimeConfigurationEntity>lambdaQuery()
                        .select(RuntimeConfigurationEntity::getId, RuntimeConfigurationEntity::getName, RuntimeConfigurationEntity::getCode, RuntimeConfigurationEntity::getValue, RuntimeConfigurationEntity::getDescription)
                        .eq(RuntimeConfigurationEntity::getSubject, subject)
        );
        List<DataPairRecord> dataPairRecords = new ArrayList<>(entities.size());
        for (RuntimeConfigurationEntity entity : entities) {
            DataPairRecord dataPairRecord = new DataPairRecord(entity.getId(), entity.getName(), entity.getCode(), entity.getValue(), entity.getDescription());
            dataPairRecords.add(dataPairRecord);
        }
        return dataPairRecords;
    }

    /**
     * 获取配置
     */
    public Properties getConfiguration(String subject) {
        List<RuntimeConfigurationEntity> entities = this.list(
                Wrappers.<RuntimeConfigurationEntity>lambdaQuery()
                        .select(RuntimeConfigurationEntity::getCode, RuntimeConfigurationEntity::getValue)
                        .eq(RuntimeConfigurationEntity::getSubject, subject)
        );
        Properties properties = new Properties();
        for (RuntimeConfigurationEntity entity : entities) {
            properties.setProperty(entity.getCode(), entity.getValue());
        }
        return properties;
    }

    /**
     * 更新配置
     *
     * @param runtimeConfigurations 配置列表
     */
    @Transactional
    public void updateConfigurations(List<RuntimeConfiguration> runtimeConfigurations) {
        if (Objects.isNull(runtimeConfigurations)) {
            throw new InvalidParameterException("配置列表不能为空");
        }
        List<RuntimeConfigurationEntity> entities = BeanUtils.copyToList(runtimeConfigurations, RuntimeConfigurationEntity.class);
        boolean ok = this.updateBatchById(entities);
        if (!ok) {
            throw new InvalidParameterException("更新配置失败");
        }
    }

    @Transactional
    public void reset(String subject) {
        List<RuntimeConfigurationEntity> configurationEntities = this.list(
                Wrappers.<RuntimeConfigurationEntity>lambdaQuery()
                        .eq(RuntimeConfigurationEntity::getSubject, subject)
        );

        configurationEntities.forEach(item -> {
            item.setValue(item.getDefaultValue());
        });

        boolean ok = this.updateBatchById(configurationEntities);
        if (!ok) {
            throw new InvalidParameterException("重置配置失败");
        }
    }
}
