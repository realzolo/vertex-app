package com.onezol.vertx.framework.component.upvote.service;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.onezol.vertx.framework.common.constant.CacheKey;
import com.onezol.vertx.framework.common.mvc.service.BaseServiceImpl;
import com.onezol.vertx.framework.component.upvote.constant.enumeration.UpvoteObjectType;
import com.onezol.vertx.framework.component.upvote.mapper.UpvoteRecordMapper;
import com.onezol.vertx.framework.component.upvote.model.dto.UpvoteCount;
import com.onezol.vertx.framework.component.upvote.model.dto.UpvoteStatus;
import com.onezol.vertx.framework.component.upvote.model.entity.UpvoteRecordEntity;
import com.onezol.vertx.framework.component.upvote.model.entity.UpvoteSummaryEntity;
import com.onezol.vertx.framework.support.support.RedisKeyHelper;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RAtomicLong;
import org.redisson.api.RLock;
import org.redisson.api.RMap;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class UpvoteRecordService extends BaseServiceImpl<UpvoteRecordMapper, UpvoteRecordEntity> {

    private static final int LOCK_TIMEOUT = 5;                  // 锁超时时间
    private static final int CACHE_BASE_EXPIRE_TIME = 30;       // 缓存过期时间（分钟）
    private static final int CACHE_RANDOM_EXPIRE_TIME = 10;     // 缓存随机过期时间（分钟）
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
        String lockKey = RedisKeyHelper.buildCacheKey(CacheKey.UPVOTE_LOCK, objectType.getValue(), objectId.toString());
        RLock lock = redissonClient.getLock(lockKey);

        try {
            lock.lock(LOCK_TIMEOUT, TimeUnit.SECONDS);

            boolean hasVoted = this.hasVoted(objectType, objectId, userId);
            int delta = hasVoted ? -1 : 1;

            // 更新数据库
            this.doToggleVote(objectType, objectId, userId, hasVoted);

            // 更新缓存
            this.updateCache(objectType, objectId, userId, delta);
        } catch (Exception e) {
            log.error("[点赞] 点赞异常", e);
            // 处理异常，避免锁未释放
            if (lock.isHeldByCurrentThread()) {
                lock.unlock();
            }
            throw e;
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
        String cacheKey = RedisKeyHelper.buildCacheKey(CacheKey.UPVOTE_COUNT, objectType.getValue(), objectId.toString());
        RAtomicLong countCache = redissonClient.getAtomicLong(cacheKey);

        // 缓存命中
        if (countCache.get() > 0) {
            return countCache.get();
        }

        // 缓存未命中查DB
        UpvoteSummaryEntity summary = upvoteSummaryService.getOne(objectType, objectId);

        // 回写缓存
        long count = 0;
        if (summary != null) {
            count = summary.getCount();
        }
        countCache.set(count);

        // 设置随机缓存过期时间，避免缓存雪崩
        long randomExpireTime = CACHE_BASE_EXPIRE_TIME + (long) (Math.random() * CACHE_RANDOM_EXPIRE_TIME);
        countCache.expire(Duration.ofMinutes(randomExpireTime));

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
        String cacheKey = RedisKeyHelper.buildCacheKey(CacheKey.UPVOTE_STATUS, objectType.getValue(), objectId.toString());
        RMap<Long, Integer> statusMap = redissonClient.getMap(cacheKey);

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

        // 设置缓存过期时间
        long randomExpireTime = CACHE_BASE_EXPIRE_TIME + (long) (Math.random() * CACHE_RANDOM_EXPIRE_TIME);
        statusMap.expire(Duration.ofMinutes(randomExpireTime));

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

    public void updateCache(UpvoteObjectType objectType, Long objectId, Long userId, int delta) {
        String statusKey = RedisKeyHelper.buildCacheKey(CacheKey.UPVOTE_STATUS, objectType.getValue(), objectId.toString());
        String countKey = RedisKeyHelper.buildCacheKey(CacheKey.UPVOTE_COUNT, objectType.getValue(), objectId.toString());
        int statusValue = delta > 0 ? 1 : 0;

        // 更新状态缓存
        RMap<Long, Integer> statusMap = redissonClient.getMap(statusKey);
        statusMap.put(userId, statusValue);

        // 更新计数缓存
        RAtomicLong countCache = redissonClient.getAtomicLong(countKey);
        countCache.addAndGet(delta);
        if (countCache.get() < 0) {
            countCache.set(0);
        }

        // 设置缓存过期时间
        long randomExpireTime = CACHE_BASE_EXPIRE_TIME + (long) (Math.random() * CACHE_RANDOM_EXPIRE_TIME);
        statusMap.expire(Duration.ofMinutes(randomExpireTime));
        countCache.expire(Duration.ofMinutes(randomExpireTime));
    }

}
