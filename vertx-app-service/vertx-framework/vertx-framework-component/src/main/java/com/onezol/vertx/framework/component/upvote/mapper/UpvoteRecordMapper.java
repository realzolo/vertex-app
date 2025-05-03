package com.onezol.vertx.framework.component.upvote.mapper;

import com.onezol.vertx.framework.common.mvc.mapper.BaseMapper;
import com.onezol.vertx.framework.component.upvote.model.dto.UpvoteRecord;
import com.onezol.vertx.framework.component.upvote.model.entity.UpvoteRecordEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface UpvoteRecordMapper extends BaseMapper<UpvoteRecordEntity> {

    /**
     * 批量插入或更新
     */
    void batchInsertOrUpdate(@Param("records") List<UpvoteRecord> records);

}
