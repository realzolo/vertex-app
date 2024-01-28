package com.onezol.vertex.framework.support.config;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.BlockAttackInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.OptimisticLockerInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import com.onezol.vertex.framework.common.bean.AuthenticationContext;
import com.onezol.vertex.framework.common.model.pojo.AuthUserModel;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDateTime;
import java.util.Objects;

@Configuration
public class MyBatisPlusConfig implements MetaObjectHandler {
    @Value("${spring.datasource.url}")
    private String url;

    @Override
    public void insertFill(MetaObject metaObject) {
        AuthUserModel authUserModel = AuthenticationContext.get();
        this.strictInsertFill(metaObject, "creator", String.class, Objects.nonNull(authUserModel) ? authUserModel.getUserCode() : "");
        this.strictInsertFill(metaObject, "createTime", LocalDateTime.class, LocalDateTime.now());
        this.strictInsertFill(metaObject, "updater", String.class, Objects.nonNull(authUserModel) ? authUserModel.getUserCode() : "");
        this.strictInsertFill(metaObject, "updateTime", LocalDateTime.class, LocalDateTime.now());
        this.strictInsertFill(metaObject, "deleted", Boolean.class, Boolean.FALSE);
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        AuthUserModel authUserModel = AuthenticationContext.get();
        this.strictUpdateFill(metaObject, "updater", String.class, Objects.nonNull(authUserModel) ? authUserModel.getUserCode() : "");
        this.strictUpdateFill(metaObject, "updateTime", LocalDateTime.class, LocalDateTime.now());
    }

    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();

        // 分页插件
        interceptor.addInnerInterceptor(new PaginationInnerInterceptor(this.getDbType()));
        // 乐观锁插件
        interceptor.addInnerInterceptor(new OptimisticLockerInnerInterceptor());
        // 防全表更新与删除插件
        interceptor.addInnerInterceptor(new BlockAttackInnerInterceptor());

        return interceptor;
    }

    /**
     * 获取数据库类型
     *
     * @return DbType
     */
    private DbType getDbType() {
        if (url.contains("mysql")) {
            return DbType.MYSQL;
        } else if (url.contains("postgresql")) {
            return DbType.POSTGRE_SQL;
        } else {
            throw new RuntimeException("不支持的数据库类型");
        }
    }
}
