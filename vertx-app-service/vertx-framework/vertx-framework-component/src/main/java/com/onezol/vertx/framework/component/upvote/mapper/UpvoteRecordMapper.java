package com.onezol.vertx.framework.component.upvote.mapper;

import com.onezol.vertx.framework.common.skeleton.mapper.BaseMapper;
import com.onezol.vertx.framework.component.upvote.constant.enumeration.UpvoteObjectType;
import com.onezol.vertx.framework.component.upvote.model.dto.UpvoteCount;
import com.onezol.vertx.framework.component.upvote.model.dto.UpvoteStatus;
import com.onezol.vertx.framework.component.upvote.model.entity.UpvoteRecordEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface UpvoteRecordMapper extends BaseMapper<UpvoteRecordEntity> {

    /**
     * 批量获取点赞数量
     *
     * @param objectType 点赞对象类型
     * @param objectIds  点赞对象ID列表
     */
    List<UpvoteCount> getVoteCounts(@Param("objectType") UpvoteObjectType objectType, @Param("objectIds") List<Long> objectIds);

    /**
     * 批量获取用户点赞状态
     *
     * @param objectType 点赞对象类型
     * @param objectIds  点赞对象ID列表
     * @param userId     用户ID
     */
    List<UpvoteStatus> hasVotedBatch(@Param("objectType") UpvoteObjectType objectType, @Param("objectIds") List<Long> objectIds, @Param("userId") Long userId);

}
