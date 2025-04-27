package com.onezol.vertex.framework.security.biz.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.onezol.vertex.framework.common.constant.CacheKey;
import com.onezol.vertex.framework.common.exception.InvalidParameterException;
import com.onezol.vertex.framework.common.model.PagePack;
import com.onezol.vertex.framework.common.util.DateUtils;
import com.onezol.vertex.framework.security.api.enumeration.LoginType;
import com.onezol.vertex.framework.security.api.model.LoginUserDetails;
import com.onezol.vertex.framework.security.api.model.dto.LoginUser;
import com.onezol.vertex.framework.security.api.model.entity.LoginHistoryEntity;
import com.onezol.vertex.framework.security.api.model.entity.UserEntity;
import com.onezol.vertex.framework.security.api.service.LoginHistoryService;
import com.onezol.vertex.framework.security.api.service.LoginUserService;
import com.onezol.vertex.framework.security.api.service.UserInfoService;
import com.onezol.vertex.framework.support.cache.RedisCache;
import com.onezol.vertex.framework.support.support.JWTHelper;
import com.onezol.vertex.framework.support.support.RedisKeyHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Component
public class LoginUserServiceImpl implements LoginUserService {

    private final RedisCache redisCache;
    private final UserInfoService userInfoService;
    private final LoginHistoryService loginHistoryService;

    @Value("${spring.jwt.expiration-time:3600}")
    private Integer expirationTime;

    public LoginUserServiceImpl(RedisCache redisCache, @Lazy UserInfoService userInfoService, LoginHistoryService loginHistoryService) {
        this.redisCache = redisCache;
        this.userInfoService = userInfoService;
        this.loginHistoryService = loginHistoryService;
    }

    /**
     * 添加在线用户
     *
     * @param loginUserDetails 登录用户信息
     * @param token            token
     */
    @Override
    public void addLoginUser(LoginUserDetails loginUserDetails, String token) {
        String subject = JWTHelper.getSubjectFromToken(token);

        // 存储用户Token
        String redisTokenKey = RedisKeyHelper.buildCacheKey(CacheKey.USER_TOKEN, subject);
        redisCache.setCacheObject(redisTokenKey, token, expirationTime, TimeUnit.SECONDS);

        // 存储用户信息
        String redisInfoKey = RedisKeyHelper.buildCacheKey(CacheKey.USER_INFO, String.valueOf(loginUserDetails.getId()));
        redisCache.setCacheObject(redisInfoKey, loginUserDetails, expirationTime, TimeUnit.SECONDS);

        // 存储在线用户
        redisCache.setCacheMapValue(CacheKey.ONLINE_USER, subject, Instant.now(Clock.systemDefaultZone()).toEpochMilli());
    }

    /**
     * 移除在线用户
     *
     * @param userId 用户ID
     */
    @Override
    public void removeLoginUser(Long userId) {
        if (userId == null) {
            throw new InvalidParameterException("用户ID不能为空");
        }

        String subject = String.valueOf(userId);

        // 移除用户Token
        String redisTokenKey = RedisKeyHelper.buildCacheKey(CacheKey.USER_TOKEN, subject);
        redisCache.deleteObject(redisTokenKey);

        // 移除用户信息
        String redisInfoKey = RedisKeyHelper.buildCacheKey(CacheKey.USER_INFO, subject);
        redisCache.deleteObject(redisInfoKey);

        // 移除用户在线用户
        redisCache.deleteCacheMapValue(CacheKey.ONLINE_USER, subject);
    }

    /**
     * 分页查询在线用户
     *
     * @param pageNumber 页码
     * @param pageSize   每页大小
     * @return 在线用户信息
     */
    @Override
    public PagePack<LoginUser> getLoginUserPage(long pageNumber, long pageSize) {
        Page<UserEntity> page = new Page<>(pageNumber, pageSize);

        // 查询在线用户
        Map<String, Object> cacheMap = redisCache.getCacheMap(CacheKey.ONLINE_USER);
        if (cacheMap == null || cacheMap.isEmpty()) {
            return PagePack.empty();
        }

        // 移除过期的用户
        List<Long> userIds = new ArrayList<>(cacheMap.keySet().stream().map(Long::parseLong).toList());
        Collection<String> keys = redisCache.keys(RedisKeyHelper.getWildcardKey(CacheKey.USER_TOKEN));
        userIds.removeIf(userId -> !keys.contains(RedisKeyHelper.buildCacheKey(CacheKey.USER_TOKEN, String.valueOf(userId))));

        if (userIds.isEmpty()) {
            return PagePack.empty();
        }

        // 查询用户信息
        Page<UserEntity> userEntityPage = userInfoService.page(
                page,
                Wrappers.<UserEntity>lambdaQuery()
                        .select(UserEntity::getId, UserEntity::getUsername, UserEntity::getNickname, UserEntity::getAvatar, UserEntity::getStatus)
                        .in(UserEntity::getId, userIds)
                        .orderByDesc(UserEntity::getUpdateTime)
        );

        // 查询登录记录
        List<LoginHistoryEntity> loginHistoryEntities = loginHistoryService.getUserLoginDetails(userIds);
        Map<Long, LoginHistoryEntity> loginHistoryMap = loginHistoryEntities.stream().collect(
                Collectors.toMap(LoginHistoryEntity::getUserId, Function.identity())
        );

        IPage<LoginUser> loginUserIPage = userEntityPage.convert(
                entity -> {
                    LoginHistoryEntity loginHistory = loginHistoryMap.get(entity.getId());
                    LoginType loginType = loginHistory.getLoginType();
                    long loginTime = Long.parseLong(cacheMap.get(String.valueOf(entity.getId())).toString());
                    String onlineTime = DateUtils.shortTimeDifference(LocalDateTime.ofInstant(Instant.ofEpochMilli(loginTime), ZoneId.of("Asia/Shanghai")), LocalDateTime.now());
                    LoginUser loginUser = new LoginUser();
                    loginUser.setUserId(entity.getId());
                    loginUser.setUsername(entity.getUsername());
                    loginUser.setNickname(entity.getNickname());
                    loginUser.setAvatar(entity.getAvatar());
                    loginUser.setLoginType(loginType.getName());
                    loginUser.setLoginTime(entity.getUpdateTime());
                    loginUser.setIp(loginHistory.getIp());
                    loginUser.setBrowser(loginHistory.getBrowser());
                    loginUser.setOs(loginHistory.getOs());
                    loginUser.setLocation(loginHistory.getLocation());
                    loginUser.setOnlineTime(onlineTime);
                    return loginUser;
                }
        );

        return PagePack.from(loginUserIPage);
    }

    /**
     * 获取在线用户总数
     *
     * @return 在线用户总数
     */
    @Override
    public Long getLoginUserCount() {
        return redisCache.getMapSize(CacheKey.ONLINE_USER);
    }

}