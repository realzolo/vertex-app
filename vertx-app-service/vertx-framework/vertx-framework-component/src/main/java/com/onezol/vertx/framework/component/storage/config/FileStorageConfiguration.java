package com.onezol.vertx.framework.component.storage.config;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.onezol.vertx.framework.component.storage.annotation.StorageType;
import com.onezol.vertx.framework.component.storage.mapper.StorageStrategyMapper;
import com.onezol.vertx.framework.component.storage.model.StorageStrategyEntity;
import org.dromara.x.file.storage.spring.EnableFileStorage;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.lang.NonNull;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Objects;

import static com.onezol.vertx.framework.component.storage.constant.StorageConstants.LOCAL_FILE_STORAGE_PREFIX;

@Configuration
@EnableFileStorage
public class FileStorageConfiguration {

    private final StorageStrategyMapper storageStrategyMapper;

    public FileStorageConfiguration(StorageStrategyMapper storageStrategyMapper) {
        this.storageStrategyMapper = storageStrategyMapper;
    }

    @Bean
    public WebMvcConfigurer localStorageWebMvcConfigurer() {
        StorageStrategyEntity localStorageStrategy = this.getLocalStorageStrategy();
        if (Objects.isNull(localStorageStrategy)) {
            return null;
        }

        return new WebMvcConfigurer() {
            @Override
            public void addResourceHandlers(@NonNull ResourceHandlerRegistry registry) {
                registry.addResourceHandler(LOCAL_FILE_STORAGE_PREFIX + "/**")
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
