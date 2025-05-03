package com.onezol.vertx.framework.component.upvote.service;

import com.onezol.vertx.framework.common.mvc.service.BaseServiceImpl;
import com.onezol.vertx.framework.component.upvote.constant.enumeration.UpvoteObjectType;
import com.onezol.vertx.framework.component.upvote.mapper.UpvoteSummaryMapper;
import com.onezol.vertx.framework.component.upvote.model.entity.UpvoteSummaryEntity;
import org.springframework.stereotype.Service;

@Service
public class UpvoteSummaryService extends BaseServiceImpl<UpvoteSummaryMapper, UpvoteSummaryEntity> {

    /**
     * 获取点赞数量
     *
     * @param objectType 点赞对象类型
     * @param objectId   点赞对象ID
     */
    public UpvoteSummaryEntity getOne(UpvoteObjectType objectType, Long objectId) {
        return this.lambdaQuery()
                .eq(UpvoteSummaryEntity::getObjectType, objectType)
                .eq(UpvoteSummaryEntity::getObjectId, objectId)
                .one();
    }

    /**
     * 获取点赞数量
     *
     * @param objectType 点赞对象类型
     * @param objectId   点赞对象ID
     */
    public Long getCount(UpvoteObjectType objectType, Long objectId) {
        return this.lambdaQuery()
                .eq(UpvoteSummaryEntity::getObjectType, objectType)
                .eq(UpvoteSummaryEntity::getObjectId, objectId)
                .count();
    }

    /**
     * 更新点赞数量
     *
     * @param objectType 点赞对象类型
     * @param objectId   点赞对象ID
     * @param delta      +1为点赞，-1为取消点赞
     */
    public void updateCount(UpvoteObjectType objectType, Long objectId, int delta) {
        UpvoteSummaryEntity entity = this.getOne(objectType, objectId);
        if (entity == null) {
            entity = new UpvoteSummaryEntity();
            entity.setObjectType(objectType);
            entity.setObjectId(objectId);
            entity.setCount(Long.parseLong(String.valueOf(delta)));
            this.save(entity);
        } else {
            entity.setCount(entity.getCount() + delta);
            this.updateById(entity);
        }
    }
}
