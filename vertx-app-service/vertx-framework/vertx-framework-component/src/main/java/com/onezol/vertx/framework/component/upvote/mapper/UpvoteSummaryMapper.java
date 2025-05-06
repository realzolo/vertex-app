package com.onezol.vertx.framework.component.upvote.mapper;

import com.onezol.vertx.framework.common.skeleton.mapper.BaseMapper;
import com.onezol.vertx.framework.component.upvote.model.entity.UpvoteSummaryEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface UpvoteSummaryMapper extends BaseMapper<UpvoteSummaryEntity> {

    /**
     * 更新点赞数量
     *
     * @param objectType 对象类型
     * @param objectId   对象ID
     * @param increment  增加的数量
     */
    @Deprecated
    int updateCount(@Param("objectType") String objectType, @Param("objectId") Long objectId, @Param("increment") int increment);

}
