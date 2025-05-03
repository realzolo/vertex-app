package com.onezol.vertx.framework.component.upvote.service;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.onezol.vertx.framework.common.mvc.service.BaseServiceImpl;
import com.onezol.vertx.framework.component.upvote.constant.enumeration.UpvoteObjectType;
import com.onezol.vertx.framework.component.upvote.mapper.UpvoteRecordMapper;
import com.onezol.vertx.framework.component.upvote.model.dto.UpvoteCount;
import com.onezol.vertx.framework.component.upvote.model.dto.UpvoteStatus;
import com.onezol.vertx.framework.component.upvote.model.entity.UpvoteRecordEntity;
import com.onezol.vertx.framework.component.upvote.model.entity.UpvoteSummaryEntity;
import org.redisson.api.RAtomicLong;
import org.redisson.api.RLock;
import org.redisson.api.RMap;
import org.redisson.api.RedissonClient;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
     *
     * @param objectType 点赞对象类型
     * @param objectId   点赞对象ID
     * @param userId     用户ID
     */
    @Transactional
    public void toggleVote(UpvoteObjectType objectType, Long objectId, Long userId) {
        final String lockKey = "upvote:lock:" + objectType.getValue() + ":" + objectId;
        RLock lock = redissonClient.getLock(lockKey);

        try {
            lock.lock(3, TimeUnit.SECONDS);

            boolean hasVoted = this.hasVoted(objectType, objectId, userId);
            int delta = hasVoted ? -1 : 1;

            // 更新数据库
            this.doToggleVote(objectType, objectId, userId, hasVoted);

            // 更新缓存
            this.updateCacheAsync(objectType, objectId, userId, delta);
        } finally {
            if (lock.isHeldByCurrentThread()) {
                lock.unlock();
            }
        }
    }

    /**
     * 获取点赞数量
     *
     * @param objectType 点赞对象类型
     * @param objectId   点赞对象ID
     */
    public long getVoteCount(UpvoteObjectType objectType, Long objectId) {
        // 先查缓存
        RAtomicLong countCache = redissonClient.getAtomicLong("upvote:count:" + objectType.getValue() + ":" + objectId);

        // 缓存命中
        if (countCache.get() > 0) {
            return (int) countCache.get();
        }

        // 缓存未命中查DB
        UpvoteSummaryEntity summary = upvoteSummaryService.getOne(objectType, objectId);

        // 回写缓存
        long count = summary != null ? summary.getCount() : 0;
        countCache.set(count);

        return count;
    }

    /**
     * 获取用户点赞状态
     *
     * @param objectType 点赞对象类型
     * @param objectId   点赞对象ID
     * @param userId     用户ID
     */
    public boolean hasVoted(UpvoteObjectType objectType, Long objectId, Long userId) {
        // 先查缓存
        RMap<Long, Integer> statusMap = redissonClient.getMap("upvote:status:" + objectType.getValue() + ":" + objectId);

        // 缓存命中
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

    /**
     * 批量获取点赞数量
     *
     * @param objectType 点赞对象类型
     * @param objectIds  点赞对象ID列表
     */
    public Map<Long, Long> getVoteCounts(UpvoteObjectType objectType, List<Long> objectIds) {
        if (objectIds.isEmpty()) {
            return Collections.emptyMap();
        }
        List<UpvoteCount> voteCounts = this.baseMapper.getVoteCounts(objectType, objectIds);
        return voteCounts.stream().collect(
                HashMap::new,
                (map, count) -> map.put(count.getObjectId(), count.getCount()),
                Map::putAll
        );
    }

    /**
     * 批量获取用户点赞状态
     *
     * @param objectType 点赞对象类型
     * @param objectIds  点赞对象ID列表
     * @param userId     用户ID
     */
    public Map<Long, Boolean> hasVotedBatch(UpvoteObjectType objectType, List<Long> objectIds, Long userId) {
        if (objectIds.isEmpty()) {
            return Collections.emptyMap();
        }
        List<UpvoteStatus> upvoteStatuses = this.baseMapper.hasVotedBatch(objectType, objectIds, userId);
        return upvoteStatuses.stream().collect(
                HashMap::new,
                (map, status) -> map.put(status.getObjectId(), status.getUpvoted()),
                Map::putAll
        );
    }

    @Transactional
    public void doToggleVote(UpvoteObjectType objectType, Long objectId, Long userId, boolean hasVoted) {
        int delta = hasVoted ? -1 : 1;

        // 删除点赞记录
        if (hasVoted) {
            this.remove(
                    Wrappers.<UpvoteRecordEntity>lambdaQuery()
                            .eq(UpvoteRecordEntity::getObjectType, objectType)
                            .eq(UpvoteRecordEntity::getObjectId, objectId)
                            .eq(UpvoteRecordEntity::getUserId, userId)
            );
        }
        // 新增点赞记录
        else {
            UpvoteRecordEntity record = new UpvoteRecordEntity();
            record.setObjectType(objectType);
            record.setObjectId(objectId);
            record.setUserId(userId);
            this.save(record);
        }

        // 更新统计表
        upvoteSummaryService.updateCount(objectType, objectId, delta);
    }

    @Async
    public void updateCacheAsync(UpvoteObjectType objectType, Long objectId, Long userId, int delta) {
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
