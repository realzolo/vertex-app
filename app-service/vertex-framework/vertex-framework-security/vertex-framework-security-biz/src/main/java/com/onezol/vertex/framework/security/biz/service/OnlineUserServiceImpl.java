package com.onezol.vertex.framework.security.biz.service;

import com.onezol.vertex.framework.common.constant.CacheKey;
import com.onezol.vertex.framework.common.util.DateUtils;
import com.onezol.vertex.framework.security.api.model.dto.OnlineUser;
import com.onezol.vertex.framework.security.api.model.dto.User;
import com.onezol.vertex.framework.security.api.model.pojo.LoginUser;
import com.onezol.vertex.framework.security.api.service.OnlineUserService;
import com.onezol.vertex.framework.support.cache.RedisCache;
import com.onezol.vertex.framework.support.support.RedisKeyHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
public class OnlineUserServiceImpl implements OnlineUserService {

    private final RedisCache redisCache;

    @Value("${spring.jwt.expiration-time:3600}")
    private Integer expirationTime;

    public OnlineUserServiceImpl(RedisCache redisCache) {
        this.redisCache = redisCache;
    }

    /**
     * 添加在线用户
     *
     * @param loginUser      登录用户
     */
    @Override
    public void addOnlineUser(LoginUser loginUser) {
        Assert.notNull(loginUser, "user must not be null");

        User user = loginUser.getDetails();

        // 存储用户信息
        String infoKey = RedisKeyHelper.buildCacheKey(CacheKey.USER_INFO, String.valueOf(user.getId()));
        redisCache.setCacheObject(infoKey, loginUser, expirationTime, TimeUnit.SECONDS);
    }

    /**
     * 移除在线用户
     *
     * @param userId 用户ID
     */
    @Override
    public void removeOnlineUser(Long userId) {
        Assert.notNull(userId, "userId must not be null");

        // 移除用户Token
        String tokenKey = RedisKeyHelper.buildCacheKey(CacheKey.USER_TOKEN, String.valueOf(userId));
        redisCache.deleteObject(tokenKey);

        // 移除用户信息
        String infoKey = RedisKeyHelper.buildCacheKey(CacheKey.USER_INFO, String.valueOf(userId));
        redisCache.deleteZSet(infoKey);
    }

    /**
     * 分页查询在线用户
     *
     * @param pageNumber 页码
     * @param pageSize   每页大小
     * @return 在线用户信息
     */
    @Override
    public List<OnlineUser> getOnlineUsers(int pageNumber, int pageSize) {
        int startIndex = (pageNumber - 1) * pageSize;
        int endIndex = pageNumber * pageSize - 1;

        // 获取在线用户ID集合
        Collection<String> keys = redisCache.keys(RedisKeyHelper.getWildcardKey(CacheKey.USER_TOKEN));
        if (keys == null || keys.isEmpty()) {
            return Collections.emptyList();
        }

        // 获取在线用户信息
        List<LoginUser> loginUsers = redisCache.getCacheList(keys.stream().toList());
        if (loginUsers.isEmpty()) {
            return Collections.emptyList();
        }

        loginUsers = loginUsers.subList(startIndex, Math.min(endIndex, loginUsers.size()));

        // 用户信息转换
        List<OnlineUser> onlineUsers = new ArrayList<>(loginUsers.size());
        for (LoginUser loginUser : loginUsers) {
            User user = loginUser.getDetails();
            OnlineUser ou = OnlineUser.builder()
                    .userId(user.getId())
                    .username(user.getUsername())
                    .nickname(user.getNickname())
                    .avatar(user.getAvatar())
                    .ip(loginUser.getIp())
                    .browser(loginUser.getBrowser())
                    .os(loginUser.getOs())
                    .location(loginUser.getLocation())
                    .loginTime(loginUser.getLoginTime())
                    .onlineTime(loginUser.getLocation())
                    .onlineTime(DateUtils.shortTimeDifference(loginUser.getLoginTime(), LocalDateTime.now()))
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
        Collection<String> keys = redisCache.keys(RedisKeyHelper.getWildcardKey(CacheKey.USER_TOKEN));
        return (long) keys.size();
    }

}