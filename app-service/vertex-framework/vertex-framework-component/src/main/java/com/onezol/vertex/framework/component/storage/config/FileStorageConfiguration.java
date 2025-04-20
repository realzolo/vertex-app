package com.onezol.vertex.framework.component.storage.config;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.onezol.vertex.framework.component.storage.annotation.StorageType;
import com.onezol.vertex.framework.component.storage.mapper.StorageStrategyMapper;
import com.onezol.vertex.framework.component.storage.model.StorageStrategyEntity;
import org.dromara.x.file.storage.spring.EnableFileStorage;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.lang.NonNull;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Objects;

@Configuration
@EnableFileStorage
public class FileStorageConfiguration {

    private final StorageStrategyMapper storageStrategyMapper;

    public FileStorageConfiguration(StorageStrategyMapper storageStrategyMapper) {
        this.storageStrategyMapper = storageStrategyMapper;
    }

    @Bean
    public WebMvcConfigurer myFileStorageWebMvcConfigurer() {
        StorageStrategyEntity localStorageStrategy = this.getLocalStorageStrategy();
        if (Objects.isNull(localStorageStrategy)) {
            return null;
        }

        return new WebMvcConfigurer() {
            @Override
            public void addResourceHandlers(@NonNull ResourceHandlerRegistry registry) {
                registry.addResourceHandler("/storage/**")
                        .addResourceLocations(String.format("file:%s/", localStorageStrategy.getRootPath()));
            }
        };
    }

    private StorageStrategyEntity getLocalStorageStrategy() {
        return storageStrategyMapper.selectOne(
                Wrappers.<StorageStrategyEntity>lambdaQuery()
                        .eq(StorageStrategyEntity::getType, StorageType.LOCAL)
                        .eq(StorageStrategyEntity::getIsDefault, true)
        );
    }

}
