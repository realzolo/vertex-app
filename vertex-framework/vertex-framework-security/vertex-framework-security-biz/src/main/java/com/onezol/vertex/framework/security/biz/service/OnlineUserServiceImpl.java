package com.onezol.vertex.framework.security.biz.service;

import com.onezol.vertex.framework.common.constant.RedisKey;
import com.onezol.vertex.framework.common.util.LocalDateTimeUtils;
import com.onezol.vertex.framework.security.api.model.dto.OnlineUser;
import com.onezol.vertex.framework.security.api.model.dto.User;
import com.onezol.vertex.framework.security.api.model.pojo.LoginUser;
import com.onezol.vertex.framework.security.api.service.OnlineUserService;
import com.onezol.vertex.framework.support.cache.RedisCache;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

@Component
@Slf4j
public class OnlineUserServiceImpl implements OnlineUserService {

    private final RedisCache redisCache;

    public OnlineUserServiceImpl(RedisCache redisCache) {
        this.redisCache = redisCache;
    }

    /**
     * 添加在线用户
     *
     * @param loginUser      登录用户
     * @param expirationTime 过期时间(秒)
     */
    @Override
    public void addOnlineUser(LoginUser loginUser, Integer expirationTime) {
        Assert.notNull(loginUser, "user must not be null");
        String key = loginUser.getKey();
        LocalDateTime loginTime = loginUser.getLoginTime();
        long loginTimeMillis = loginTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();

        // 存储用户信息
        redisCache.setCacheObject(RedisKey.USER + key, loginUser, expirationTime, TimeUnit.SECONDS);
        // 存储用户ID(key为MD5加密后的用户ID, value为登录时间)
        Map<String, Double> zSet = new HashMap<>() {{
            put(key, (double) loginTimeMillis);
        }};
        redisCache.setCacheZSet(RedisKey.USERID_SET, zSet);
    }

    /**
     * 移除在线用户
     *
     * @param userId 用户ID
     */
    @Override
    public void removeOnlineUser(Long userId) {
        Assert.notNull(userId, "userId must not be null");
        String key = String.valueOf(userId);
        removeOnlineUser(key);
    }

    /**
     * 移除在线用户
     *
     * @param key MD5加密后的用户ID
     */
    @Override
    public void removeOnlineUser(String key) {
        // 移除用户信息
        redisCache.deleteObject(RedisKey.USER + key);
        // 移除用户ID
        redisCache.deleteZSet(RedisKey.USERID_SET, key);
    }


    /**
     * 分页查询在线用户
     *
     * @param pageNo   页码
     * @param pageSize 每页大小
     * @return 在线用户信息
     */
    @Override
    public Set<OnlineUser> getOnlineUsers(int pageNo, int pageSize) {
        int startIndex = (pageNo - 1) * pageSize;
        int endIndex = pageNo * pageSize - 1;
        // 获取在线用户ID集合
        Set<Object> keySet = redisCache.getCacheZSet(RedisKey.USERID_SET, startIndex, endIndex);
        // 获取在线用户信息
        List<LoginUser> loginUsers = new ArrayList<>(keySet.size());
        keySet.forEach(key -> loginUsers.add(redisCache.getCacheObject(RedisKey.USER + key)));

        // 用户信息转换
        HashSet<OnlineUser> onlineUsers = new HashSet<>(loginUsers.size());
        for (LoginUser ui : loginUsers) {
            User user = ui.getUser();
            OnlineUser ou = OnlineUser.builder()
                    .userId(user.getId())
                    .username(user.getUsername())
                    .nickname(user.getNickname())
                    .avatar(user.getAvatar())
                    .ip(ui.getIp())
                    .browser(ui.getBrowser())
                    .os(ui.getOs())
                    .location(ui.getLocation())
                    .loginTime(ui.getLoginTime())
                    .onlineTime(ui.getLocation())
                    .onlineTime(LocalDateTimeUtils.shortTimeDifference(ui.getLoginTime(), LocalDateTime.now()))
                    .build();
            onlineUsers.add(ou);
        }

        return onlineUsers;
    }

    /**
     * 获取在线用户总数
     *
     * @return 在线用户总数
     */
    @Override
    public Long getOnlineUserCount() {
        return redisCache.getZSetSize(RedisKey.USERID_SET);
    }


    /**
     * 定时同步ID_SET中有效的用户
     */
    @Scheduled(cron = "0/30 * * * * ?")
    public void removeExpiredOnlineUser() {
        // 获取在线用户ID集合
        Set<Object> keySet = redisCache.getCacheZSet(RedisKey.USERID_SET, 0, -1);
        // 清除过期用户
        AtomicInteger count = new AtomicInteger();
        keySet.forEach(key -> {
            LoginUser loginUser = redisCache.getCacheObject(RedisKey.USER + key);
            if (loginUser == null) {  // 用户信息不存在, 表示用户信息已过期/被删除
                redisCache.deleteZSet(RedisKey.USERID_SET, key);
                count.getAndIncrement();
            }
        });
    }

}