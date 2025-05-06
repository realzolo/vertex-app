package com.onezol.vertx.framework.common.skeleton.mapper;

import com.onezol.vertx.framework.common.skeleton.model.BaseEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface CommonMapper extends BaseMapper<BaseEntity> {

    @Select("SELECT count(*) FROM information_schema.tables WHERE table_name = #{tableName}")
    int checkTableExistence(@Param("tableName") String tableName);

    @Select("${sql}")
    void execute(@Param("sql") String sql);

}