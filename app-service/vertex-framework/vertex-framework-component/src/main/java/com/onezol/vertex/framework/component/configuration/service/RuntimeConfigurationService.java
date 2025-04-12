package com.onezol.vertex.framework.component.configuration.service;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.onezol.vertex.framework.common.exception.RuntimeServiceException;
import com.onezol.vertex.framework.common.model.LabelValue;
import com.onezol.vertex.framework.common.model.SimpleModel;
import com.onezol.vertex.framework.common.mvc.service.BaseServiceImpl;
import com.onezol.vertex.framework.common.util.BeanUtils;
import com.onezol.vertex.framework.common.util.StringUtils;
import com.onezol.vertex.framework.component.configuration.mapper.RuntimeConfigurationMapper;
import com.onezol.vertex.framework.component.configuration.model.RuntimeConfiguration;
import com.onezol.vertex.framework.component.configuration.model.RuntimeConfigurationEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Service
public class RuntimeConfigurationService extends BaseServiceImpl<RuntimeConfigurationMapper, RuntimeConfigurationEntity> {

    /**
     * 获取配置列表
     *
     * @param subject 配置主题
     */
    public List<LabelValue<String, String>> listConfigurationsAsLabelValue(String subject) {
        List<RuntimeConfigurationEntity> configurationEntities = this.list(
                Wrappers.<RuntimeConfigurationEntity>lambdaQuery()
                        .eq(RuntimeConfigurationEntity::getSubject, subject)
        );
        return configurationEntities.stream().map(
                entity -> new LabelValue<>(entity.getCode(), StringUtils.isNotEmpty(entity.getValue()) ? entity.getValue() : entity.getDefaultValue())
        ).toList();
    }

    /**
     * 获取配置列表
     *
     * @param subject 配置主题
     */
    public List<SimpleModel<String>> listConfigurationsAsSimpleModel(String subject) {
        List<RuntimeConfigurationEntity> configurationEntities = this.list(
                Wrappers.<RuntimeConfigurationEntity>lambdaQuery()
                        .eq(RuntimeConfigurationEntity::getSubject, subject)
        );
        return configurationEntities.stream().map(
                entity -> SimpleModel.of(entity.getId(), entity.getName(), entity.getCode(), entity.getValue(), entity.getDescription())
        ).toList();
    }

    /**
     * 获取配置列表
     *
     * @param subject 配置主题
     */
    public List<RuntimeConfiguration> listConfigurations(String subject) {
        List<RuntimeConfigurationEntity> configurationEntities = this.list(
                Wrappers.<RuntimeConfigurationEntity>lambdaQuery()
                        .eq(RuntimeConfigurationEntity::getSubject, subject)
        );
        configurationEntities.forEach(item -> {
            item.setValue(StringUtils.isNotEmpty(item.getValue()) ? item.getValue() : item.getDefaultValue());
        });
        return BeanUtils.toList(configurationEntities, RuntimeConfiguration.class);
    }

    /**
     * 更新配置
     *
     * @param runtimeConfigurations 配置列表
     */
    @Transactional
    public void updateConfigurations(List<RuntimeConfiguration> runtimeConfigurations) {
        if (Objects.isNull(runtimeConfigurations)) {
            throw new RuntimeServiceException("配置列表不能为空");
        }
        List<RuntimeConfigurationEntity> entities = BeanUtils.toList(runtimeConfigurations, RuntimeConfigurationEntity.class);
        boolean ok = this.updateBatchById(entities);
        if (!ok) {
            throw new RuntimeServiceException("更新配置失败");
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
            throw new RuntimeServiceException("重置配置失败");
        }
    }
}
