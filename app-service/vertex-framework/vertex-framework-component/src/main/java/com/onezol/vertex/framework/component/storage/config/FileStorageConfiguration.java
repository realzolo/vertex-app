package com.onezol.vertex.framework.component.storage.config;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.onezol.vertex.framework.component.storage.annotation.StorageTypeEnum;
import com.onezol.vertex.framework.component.storage.mapper.StorageStrategyMapper;
import com.onezol.vertex.framework.component.storage.model.StorageStrategyEntity;
import org.dromara.x.file.storage.spring.EnableFileStorage;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.lang.NonNull;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@EnableFileStorage
@Configuration
public class FileStorageConfiguration {
    private final StorageStrategyMapper storageStrategyMapper;

    public FileStorageConfiguration(StorageStrategyMapper storageStrategyMapper) {
        this.storageStrategyMapper = storageStrategyMapper;
    }

//    @Bean
    public WebMvcConfigurer myFileStorageWebMvcConfigurer() {
        StorageStrategyEntity strategy = storageStrategyMapper.selectOne(
                Wrappers.<StorageStrategyEntity>lambdaQuery()
                        .eq(StorageStrategyEntity::getType, StorageTypeEnum.LOCAL)
                        .eq(StorageStrategyEntity::getIsDefault, true)
        );
        return new WebMvcConfigurer() {
            @Override
            public void addResourceHandlers(@NonNull ResourceHandlerRegistry registry) {
                registry.addResourceHandler("/storage/**")
                        .addResourceLocations("file:" + strategy.getBasePath());
            }
        };
    }

}
