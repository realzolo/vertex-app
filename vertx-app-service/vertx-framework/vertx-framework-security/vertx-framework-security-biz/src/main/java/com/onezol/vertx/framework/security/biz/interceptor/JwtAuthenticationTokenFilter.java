package com.onezol.vertx.framework.security.biz.interceptor;

import com.onezol.vertx.framework.common.constant.CacheKey;
import com.onezol.vertx.framework.common.util.StringUtils;
import com.onezol.vertx.framework.security.api.model.LoginUserDetails;
import com.onezol.vertx.framework.support.cache.RedisCache;
import com.onezol.vertx.framework.support.support.JWTHelper;
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
 * JWT token过滤器: 验证token有效性、token续期
 */
@Slf4j
@Component
public class JwtAuthenticationTokenFilter extends OncePerRequestFilter {

    private final RedisCache redisCache;

    @Value("${spring.jwt.expiration-time:3600}")
    private Integer expirationTime;     // token过期时间: 默认1小时 （单位秒）

    @Value("${spring.jwt.renew-threshold:900}")
    private Integer renewThreshold;     // token续期的阈值: 默认15分钟 （单位秒, 距离过期时间小于该阈值时, 进行续期操作）

    public JwtAuthenticationTokenFilter(RedisCache redisCache) {
        this.redisCache = redisCache;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain) throws IOException, ServletException {
        // 从请求头中获取token
        String authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (StringUtils.isNotBlank(authorizationHeader) && authorizationHeader.startsWith(AUTHORIZATION_HEADER)) {
            String token = authorizationHeader.substring(AUTHORIZATION_HEADER.length());

            // 1. 校验token
            boolean ok = JWTHelper.validateToken(token);
            if (!ok) {
                filterChain.doFilter(request, response);
                return;
            }

            // 2. 从Redis中获取用户信息, 并验证用户信息是否存在
            String subject = JWTHelper.getSubjectFromToken(token);
            String redisTokenKey = RedisKeyHelper.buildCacheKey(CacheKey.USER_TOKEN, subject);
            String redisInfoKey = RedisKeyHelper.buildCacheKey(CacheKey.USER_INFO, subject);
            String tokenInRedis = redisCache.getCacheObject(redisTokenKey);
            LoginUserDetails loginUserDetails = redisCache.getCacheObject(redisInfoKey);
            if (StringUtils.isBlank(tokenInRedis) || Objects.isNull(loginUserDetails)) {
                filterChain.doFilter(request, response);
                return;
            }

            // 检查token是否快要过期，进行续期操作
            if (this.checkTokenIsAboutToExpire(token)) {
                String renewedToken = this.renewalToken(subject);
                response.setHeader(HttpHeaders.AUTHORIZATION, AUTHORIZATION_HEADER + renewedToken);
            }

            // 将用户信息存入SecurityContext中, 以便后续使用
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(loginUserDetails, null, loginUserDetails.getAuthorities());
            authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        }

        // 放行
        filterChain.doFilter(request, response);
    }

    /**
     * 检查token是否快要过期
     *
     * @param token JWT
     * @return true: 快要过期, false: 没有快要过期
     */
    private boolean checkTokenIsAboutToExpire(String token) {
        Claims claims = JWTHelper.getClaimsFromToken(token);
        assert claims != null;
        Date expirationDate = claims.getExpiration();
        long timeToExpiration = expirationDate.getTime() - System.currentTimeMillis();
        return timeToExpiration < renewThreshold * 1000;
    }

    /**
     * 续期JWT（重新生成一个）
     *
     * @param userId 用户名
     * @return 续期后的JWT
     */
    private String renewalToken(String userId) {
        String renewedToken = JWTHelper.generateToken(userId);
        String redisTokenKey = RedisKeyHelper.buildCacheKey(CacheKey.USER_TOKEN, userId);
        String redisInfoKey = RedisKeyHelper.buildCacheKey(CacheKey.USER_INFO, userId);
        redisCache.setCacheObject(redisTokenKey, renewedToken, expirationTime, TimeUnit.SECONDS);
        redisCache.expire(redisInfoKey, expirationTime, TimeUnit.SECONDS);
        log.info("[Security] 用户({})的token已续期", userId);
        return renewedToken;
    }

}
