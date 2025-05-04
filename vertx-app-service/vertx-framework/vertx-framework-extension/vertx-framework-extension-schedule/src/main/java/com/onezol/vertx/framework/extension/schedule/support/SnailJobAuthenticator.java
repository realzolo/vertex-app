package com.onezol.vertx.framework.extension.schedule.support;

import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import com.aizuda.snailjob.common.core.model.Result;
import com.alibaba.druid.support.json.JSONUtils;
import com.alibaba.fastjson2.JSONObject;
import com.onezol.vertx.framework.common.constant.CacheKey;
import com.onezol.vertx.framework.common.util.EncryptUtils;
import com.onezol.vertx.framework.common.util.JsonUtils;
import com.onezol.vertx.framework.support.cache.RedisCache;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * 任务调度认证器，负责与任务调度中心进行认证交互并获取认证令牌。
 * 该类通过 HTTP 请求向任务调度中心发送认证信息，获取 JWT 令牌，
 * 并将令牌缓存到 Redis 中，设置合适的过期时间。
 */
@Slf4j
@Component
public class SnailJobAuthenticator {

    /**
     * 认证成功的状态码
     */
    public static final Integer STATUS_SUCCESS = 1;

    /**
     * 认证登录的 API 路径
     */
    private static final String AUTH_URL = "/auth/login";

    private final RedisCache redisCache;

    @Value("${snail-job.server.api.url}")
    private String url;

    @Value("${snail-job.server.api.username}")
    private String username;

    @Value("${snail-job.server.api.password}")
    private String password;

    /**
     * 构造函数，注入 Redis 缓存服务实例。
     *
     * @param redisCache Redis 缓存服务实例
     */
    public SnailJobAuthenticator(RedisCache redisCache) {
        this.redisCache = redisCache;
    }

    /**
     * 获取认证令牌。
     * 首先尝试从 Redis 缓存中获取令牌，如果缓存中不存在，则进行认证操作获取新令牌，
     * 并将新令牌解析后缓存到 Redis 中，设置合适的过期时间。
     *
     * @return 认证令牌
     */
    public String getToken() {
        String token = redisCache.getCacheObject(CacheKey.SNAIL_JOB_AUTH_TOKEN);
        if (StrUtil.isBlank(token)) {
            token = authenticate();
            try {
                // 不验证签名解析 Token
                String[] parts = token.split("\\.");
                if (parts.length != 3) {
                    throw new IllegalArgumentException("无效的 JWT 令牌");
                }
                String payloadBase64 = parts[1];
                String payloadJson = new String(java.util.Base64.getUrlDecoder().decode(payloadBase64));
                JSONObject payload = JSONObject.parseObject(payloadJson);
                long expiresAt = payload.getLongValue("exp") * 1000; // 转换为毫秒
                long currentTime = System.currentTimeMillis();
                long ttl = (expiresAt - currentTime) / 1000 - 60; // 剩余时间（秒）（提前 1 分钟）

                if (ttl > 0) {
                    redisCache.setCacheObject(CacheKey.SNAIL_JOB_AUTH_TOKEN, token, (int) ttl, java.util.concurrent.TimeUnit.SECONDS);
                }
            } catch (Exception e) {
                log.error("[SnailJobAuthenticator][获取 Token] 解析 Token 失败", e);
            }
        }
        return token;
    }

    /**
     * 执行密码认证操作，向任务调度中心发送认证请求并获取令牌。
     *
     * @return 认证成功后返回的令牌
     * @throws IllegalStateException 当连接任务调度中心异常或认证失败时抛出
     */
    private String authenticate() {
        Map<String, Object> paramMap = MapUtil.newHashMap(2);
        paramMap.put("username", username);
        paramMap.put("password", EncryptUtils.encryptWithMD5(password));

        HttpRequest httpRequest = HttpUtil.createPost(String.format("%s%s", url, AUTH_URL));
        httpRequest.body(JsonUtils.toJsonString(paramMap));

        Result<?> result;
        try (HttpResponse response = httpRequest.execute()) {
            if (!response.isOk() || response.body() == null) {
                throw new IllegalStateException("连接任务调度中心异常");
            }
            result = JsonUtils.parseObject(response.body(), Result.class);
        }

        if (!STATUS_SUCCESS.equals(result.getStatus())) {
            log.warn("密码认证失败，预期返回成功响应。错误信息：{}", result.getMessage());
            throw new IllegalStateException(result.getMessage());
        }

        Object data = result.getData();
        return JSONObject.parseObject(JSONUtils.toJSONString(data)).getString("token");
    }
}
