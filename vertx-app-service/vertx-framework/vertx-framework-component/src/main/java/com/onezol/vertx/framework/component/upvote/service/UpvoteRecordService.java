package com.onezol.vertx.framework.component.upvote.service;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.onezol.vertx.framework.common.exception.InvalidDataStateException;
import com.onezol.vertx.framework.common.mvc.service.BaseServiceImpl;
import com.onezol.vertx.framework.component.upvote.constant.enumeration.UpvoteObjectType;
import com.onezol.vertx.framework.component.upvote.mapper.UpvoteRecordMapper;
import com.onezol.vertx.framework.component.upvote.mapper.UpvoteSummaryMapper;
import com.onezol.vertx.framework.component.upvote.model.dto.UpvoteRecord;
import com.onezol.vertx.framework.component.upvote.model.entity.UpvoteRecordEntity;
import com.onezol.vertx.framework.component.upvote.model.entity.UpvoteSummaryEntity;
import org.redisson.api.RAtomicLong;
import org.redisson.api.RLock;
import org.redisson.api.RMap;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class UpvoteRecordService extends BaseServiceImpl<UpvoteRecordMapper, UpvoteRecordEntity> {

    private final RedissonClient redissonClient;
    private final UpvoteSummaryService upvoteSummaryService;

    public UpvoteRecordService(RedissonClient redissonClient, UpvoteSummaryService upvoteSummaryService) {
        this.redissonClient = redissonClient;
        this.upvoteSummaryService = upvoteSummaryService;
    }

    /**
     * 切换点赞状态
     * @param objectType 点赞对象类型
     * @param objectId 点赞对象ID
     * @param userId 用户ID
     */
    public void toggleVote(UpvoteObjectType objectType, Long objectId, Long userId) {
        final String lockKey = "upvote:lock:" + objectType.getValue() + ":" + objectId;
        RLock lock = redissonClient.getLock(lockKey);

        try {
            lock.lock(3, TimeUnit.SECONDS);
            boolean hasVoted = hasVoted(objectType, objectId, userId);
            int delta = hasVoted ? -1 : 1;

            // DB事务操作
            if (hasVoted) {
                // 删除点赞记录
                this.remove(
                        Wrappers.<UpvoteRecordEntity>lambdaQuery()
                                .eq(UpvoteRecordEntity::getObjectType, objectType)
                                .eq(UpvoteRecordEntity::getObjectId, objectId)
                                .eq(UpvoteRecordEntity::getUserId, userId)
                );
            } else {
                // 新增点赞记录
                UpvoteRecordEntity record = new UpvoteRecordEntity();
                record.setObjectType(objectType);
                record.setObjectId(objectId);
                record.setUserId(userId);
                this.save(record);
            }

            // 更新统计表
            upvoteSummaryService.updateCount(objectType, objectId, delta);

            // 更新缓存
            updateCache(objectType, objectId, userId, delta);
        } finally {
            lock.unlock();
        }
    }

    /**
     * 获取点赞数量
     * @param objectType 点赞对象类型
     * @param objectId 点赞对象ID
     */
    public long getVoteCount(UpvoteObjectType objectType, Long objectId) {
        // 先查缓存
        RAtomicLong countCache = redissonClient.getAtomicLong(
                "upvote:count:" + objectType.getValue() + ":" + objectId);
        return (int) countCache.get();
        //        if (countCache.get() > 0) {
//            return (int) countCache.get();
//        }
//
//        // 缓存未命中查DB
//        UpvoteSummaryEntity summary = upvoteSummaryService.getOne(objectType, objectId);
//        long count = summary != null ? summary.getCount() : 0;
//        countCache.set(count);
//
//        return count;
    }

    /**
     * 获取用户点赞状态
     * @param objectType 点赞对象类型
     * @param objectId 点赞对象ID
     * @param userId 用户ID
     */
    public boolean hasVoted(UpvoteObjectType objectType, Long objectId, Long userId) {
        // 先查缓存
        RMap<Long, Integer> statusMap = redissonClient.getMap("upvote:status:" + objectType.getValue() + ":" + objectId);

        if (statusMap.containsKey(userId)) {
            return statusMap.get(userId) == 1;
        }

        // 缓存未命中查DB
        long count = this.count(
                Wrappers.<UpvoteRecordEntity>lambdaQuery()
                        .eq(UpvoteRecordEntity::getObjectType, objectType.getValue())
                        .eq(UpvoteRecordEntity::getObjectId, objectId)
                        .eq(UpvoteRecordEntity::getUserId, userId)
        );

        // 回写缓存
        statusMap.put(userId, count > 0 ? 1 : 0);
        return count > 0;
    }

    private void updateCache(UpvoteObjectType objectType, Long objectId, Long userId, int delta) {
        // 更新状态缓存
        RMap<Long, Integer> statusMap = redissonClient.getMap(
                "upvote:status:" + objectType.getValue() + ":" + objectId);
        statusMap.put(userId, delta > 0 ? 1 : 0);

        // 更新计数缓存
        RAtomicLong countCache = redissonClient.getAtomicLong(
                "upvote:count:" + objectType.getValue() + ":" + objectId);
        countCache.addAndGet(delta);

        // 防止计数出现负数
        if (countCache.get() < 0) {
            countCache.set(0);
        }

        // 设置缓存过期时间
        countCache.expire(30, TimeUnit.MINUTES);
    }

}
