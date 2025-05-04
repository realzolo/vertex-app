package com.onezol.vertx.framework.security.biz.interceptor;

import com.onezol.vertx.framework.common.constant.CacheKey;
import com.onezol.vertx.framework.common.util.StringUtils;
import com.onezol.vertx.framework.security.api.model.LoginUserDetails;
import com.onezol.vertx.framework.support.cache.RedisCache;
import com.onezol.vertx.framework.support.support.JwtHelper;
import com.onezol.vertx.framework.support.support.RedisKeyHelper;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Date;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import static com.onezol.vertx.framework.common.constant.GenericConstants.AUTHORIZATION_HEADER;

/**
 * JwtValidationFilter 是一个用于验证 JWT 令牌的过滤器。
 * 该过滤器会拦截所有请求，检查请求头中的 Authorization 字段是否包含有效的 JWT 令牌。
 * 如果 JWT 令牌有效，则从 Redis 中获取用户信息，并将用户信息存入 SecurityContext 中，以便后续使用。
 * 同时，该过滤器还会检查 JWT 令牌是否即将过期，如果即将过期，则会进行续期操作。
 */
@Slf4j
@Component
public class JwtValidationFilter extends OncePerRequestFilter {

    /**
     * Redis 缓存服务，用于存储和获取用户信息和 JWT 令牌。
     */
    private final RedisCache redisCache;

    /**
     * JWT 令牌的过期时间，单位为秒，默认值为 3600 秒（即 1 小时）。
     * 可通过配置文件中的 `spring.jwt.expiration-time` 属性进行配置。
     */
    @Value("${spring.jwt.expiration-time:3600}")
    private Integer expirationTime;

    /**
     * JWT 令牌续期的阈值，单位为秒，默认值为 900 秒（即 15 分钟）。
     * 当 JWT 令牌距离过期时间小于该阈值时，会进行续期操作。
     * 可通过配置文件中的 `spring.jwt.renew-threshold` 属性进行配置。
     */
    @Value("${spring.jwt.renew-threshold:900}")
    private Integer renewThreshold;

    /**
     * 构造函数，注入 Redis 缓存服务。
     *
     * @param redisCache Redis 缓存服务实例。
     */
    public JwtValidationFilter(RedisCache redisCache) {
        this.redisCache = redisCache;
    }

    /**
     * 该方法会在每次请求时执行，用于验证请求头中的 JWT 令牌，并进行相应的处理。
     *
     * @param request     HTTP 请求对象。
     * @param response    HTTP 响应对象。
     * @param filterChain 过滤器链，用于将请求传递给下一个过滤器。
     * @throws IOException      如果在处理请求或响应时发生 I/O 错误。
     * @throws ServletException 如果在处理请求时发生 Servlet 异常。
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain) throws IOException, ServletException {
        // 从请求头中获取 JWT 令牌
        String authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (StringUtils.isNotBlank(authorizationHeader) && authorizationHeader.startsWith(AUTHORIZATION_HEADER)) {
            String token = authorizationHeader.substring(AUTHORIZATION_HEADER.length());

            // 1. 校验 JWT 令牌
            boolean isValid = JwtHelper.isJwtValid(token);
            if (!isValid) {
                filterChain.doFilter(request, response);
                return;
            }

            // 2. 从 Redis 中获取用户信息，并验证用户信息是否存在
            String subject = JwtHelper.extractSubjectFromJwt(token);
            String redisTokenKey = RedisKeyHelper.buildCacheKey(CacheKey.USER_TOKEN, subject);
            String redisInfoKey = RedisKeyHelper.buildCacheKey(CacheKey.USER_INFO, subject);
            String tokenInRedis = redisCache.getCacheObject(redisTokenKey);
            LoginUserDetails loginUserDetails = redisCache.getCacheObject(redisInfoKey);
            if (StringUtils.isBlank(tokenInRedis) || Objects.isNull(loginUserDetails)) {
                filterChain.doFilter(request, response);
                return;
            }

            // 检查 JWT 令牌是否快要过期，进行续期操作
            if (this.checkTokenIsAboutToExpire(token)) {
                String renewedToken = this.renewalToken(subject);
                response.setHeader(HttpHeaders.AUTHORIZATION, AUTHORIZATION_HEADER + renewedToken);
            }

            // 将用户信息存入 SecurityContext 中，以便后续使用
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(loginUserDetails, null, loginUserDetails.getAuthorities());
            authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        }

        // 放行请求
        filterChain.doFilter(request, response);
    }

    /**
     * 检查 JWT 令牌是否即将过期。
     *
     * @param token 待检查的 JWT 令牌。
     * @return 如果 JWT 令牌即将过期，则返回 true；否则返回 false。
     */
    private boolean checkTokenIsAboutToExpire(String token) {
        Claims claims = JwtHelper.extractClaimsFromJwt(token);
        assert claims != null;
        Date expirationDate = claims.getExpiration();
        long timeToExpiration = expirationDate.getTime() - System.currentTimeMillis();
        return timeToExpiration < renewThreshold * 1000;
    }

    /**
     * 对 JWT 令牌进行续期操作，即重新生成一个新的 JWT 令牌，并更新 Redis 中的缓存信息。
     *
     * @param userId 用户的标识信息。
     * @return 续期后的 JWT 令牌。
     */
    private String renewalToken(String userId) {
        String renewedToken = JwtHelper.createJwt(userId);
        String redisTokenKey = RedisKeyHelper.buildCacheKey(CacheKey.USER_TOKEN, userId);
        String redisInfoKey = RedisKeyHelper.buildCacheKey(CacheKey.USER_INFO, userId);
        redisCache.setCacheObject(redisTokenKey, renewedToken, expirationTime, TimeUnit.SECONDS);
        redisCache.expire(redisInfoKey, expirationTime, TimeUnit.SECONDS);
        log.info("[Security] 用户({})的 JWT 令牌已续期", userId);
        return renewedToken;
    }
}
