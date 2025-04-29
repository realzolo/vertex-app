package com.onezol.vertx.framework.support.config;

import com.baomidou.mybatisplus.autoconfigure.MybatisPlusPropertiesCustomizer;
import com.baomidou.mybatisplus.core.incrementer.IdentifierGenerator;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.BlockAttackInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.OptimisticLockerInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import com.baomidou.mybatisplus.extension.toolkit.JdbcUtils;
import com.onezol.vertx.framework.security.api.context.AuthenticationContext;
import com.onezol.vertx.framework.security.api.model.UserIdentity;
import com.onezol.vertx.framework.support.support.CompactSnowflakeIdGenerator;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Objects;

@Configuration
public class MyBatisPlusConfiguration {

    @Value("${spring.datasource.url}")
    private String dataSourceUrl;

    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();

        // 分页插件
        interceptor.addInnerInterceptor(new PaginationInnerInterceptor(JdbcUtils.getDbType(dataSourceUrl)));
        // 乐观锁插件
        interceptor.addInnerInterceptor(new OptimisticLockerInnerInterceptor());
        // 防全表更新与删除插件
        interceptor.addInnerInterceptor(new BlockAttackInnerInterceptor());

        return interceptor;
    }

    @Bean
    public MybatisPlusPropertiesCustomizer mybatisPlusPropertiesCustomizer(SnowflakeIdGenerator idGenerator) {
        return properties -> {
            properties.getGlobalConfig().setIdentifierGenerator(idGenerator);
        };
    }

    @Component
    public static class SnowflakeIdGenerator implements IdentifierGenerator {
        private final CompactSnowflakeIdGenerator snowflakeGenerator;

        public SnowflakeIdGenerator(@Value("${application.snowflake.machine-id:1}") Long machineId) {
            this.snowflakeGenerator = new CompactSnowflakeIdGenerator(machineId);
        }

        @Override
        public Long nextId(Object entity) {
            return snowflakeGenerator.nextId();
        }
    }

    @Component
    public static class MetaObjectHandler implements com.baomidou.mybatisplus.core.handlers.MetaObjectHandler {
        @Override
        public void insertFill(MetaObject metaObject) {
            UserIdentity userIdentity = AuthenticationContext.get();
            this.strictInsertFill(metaObject, "creator", Long.class, Objects.nonNull(userIdentity) ? userIdentity.getUserId() : null);
            this.strictInsertFill(metaObject, "createTime", LocalDateTime.class, LocalDateTime.now());
            this.strictInsertFill(metaObject, "updater", Long.class, Objects.nonNull(userIdentity) ? userIdentity.getUserId() : null);
            this.strictInsertFill(metaObject, "updateTime", LocalDateTime.class, LocalDateTime.now());
            this.strictInsertFill(metaObject, "deleted", Boolean.class, Boolean.FALSE);
        }

        @Override
        public void updateFill(MetaObject metaObject) {
            UserIdentity userIdentity = AuthenticationContext.get();
            this.strictUpdateFill(metaObject, "updater", Long.class, Objects.nonNull(userIdentity) ? userIdentity.getUserId() : null);
            this.strictUpdateFill(metaObject, "updateTime", LocalDateTime.class, LocalDateTime.now());
        }
    }

}
