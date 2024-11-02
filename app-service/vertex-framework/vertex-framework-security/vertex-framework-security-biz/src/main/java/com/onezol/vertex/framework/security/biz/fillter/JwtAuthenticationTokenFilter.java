package com.onezol.vertex.framework.security.biz.fillter;

import com.onezol.vertex.framework.common.constant.CacheKey;
import com.onezol.vertex.framework.common.util.JwtUtils;
import com.onezol.vertex.framework.common.util.StringUtils;
import com.onezol.vertex.framework.security.api.model.pojo.LoginUser;
import com.onezol.vertex.framework.support.cache.RedisCache;
import com.onezol.vertex.framework.support.support.CacheKeyHelper;
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

import static com.onezol.vertex.framework.common.constant.GenericConstants.AUTHORIZATION_HEADER;


/**
 * JWT token过滤器: 验证token有效性、token续期
 */
@Slf4j
@Component
public class JwtAuthenticationTokenFilter extends OncePerRequestFilter {

    private final RedisCache redisCache;
    /**
     * token过期时间: 默认1小时 （单位秒）
     */
    @Value("${spring.jwt.expiration-time:3600}")
    private Integer expirationTime;

    /**
     * token续期的阈值: 默认15分钟 （单位秒, 距离过期时间小于该阈值时, 进行续期操作）
     */
    @Value("${spring.jwt.renew-threshold:900}")
    private Integer renewThreshold;


    public JwtAuthenticationTokenFilter(RedisCache redisCache) {
        this.redisCache = redisCache;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain) throws IOException, ServletException {
        // 从请求头中获取token
        String authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (StringUtils.isNotBlank(authorizationHeader) && authorizationHeader.startsWith(AUTHORIZATION_HEADER)) {
            String token = authorizationHeader.substring(AUTHORIZATION_HEADER.length());

            // 验证token有效性
            boolean ok = JwtUtils.validateToken(token);
            if (!ok) {
                filterChain.doFilter(request, response);
                return;
            }

            // 从Redis中获取用户信息, 并验证用户信息是否存在
            String subject = JwtUtils.getSubjectFromToken(token);
            String redisKey = CacheKeyHelper.buildCacheKey(CacheKey.USER_INFO, subject);
            LoginUser loginUser = redisCache.getCacheObject(redisKey);
            if (Objects.isNull(loginUser)) {
                filterChain.doFilter(request, response);
                return;
            }

            // 检查token是否快要过期，进行续期操作
            Claims claims = JwtUtils.getClaimsFromToken(token);
            assert claims != null;
            if (isTokenNearExpiration(claims)) {
                // 续期JWT
                String renewedToken = renewToken(claims);
                response.setHeader(HttpHeaders.AUTHORIZATION, AUTHORIZATION_HEADER + renewedToken);
                // 续期Redis中的用户信息
                redisCache.expire(redisKey, expirationTime, TimeUnit.SECONDS);
                log.info("用户[{}]的token已续期", subject);
            }

            // 将用户信息存入SecurityContext中, 以便后续使用
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(loginUser, null, loginUser.getAuthorities());
            authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        }

        // 放行
        filterChain.doFilter(request, response);
    }

    /**
     * 检查token是否快要过期
     *
     * @param claims JWT的Claims对象
     * @return true: 快要过期, false: 没有快要过期
     */
    private boolean isTokenNearExpiration(Claims claims) {
        // 获取token的过期时间
        Date expirationDate = claims.getExpiration();
        // 计算token剩余有效时间（单位：毫秒）
        long timeToExpiration = expirationDate.getTime() - System.currentTimeMillis();
        // 判断token是否快要过期
        return timeToExpiration < renewThreshold * 1000;
    }

    /**
     * 续期JWT（重新生成一个）
     *
     * @param claims JWT的Claims对象
     * @return 续期后的JWT
     */
    private String renewToken(Claims claims) {
        String subject = claims.getSubject();
        // 进行JWT续期操作(重新生成一个)
        return JwtUtils.generateToken(subject);
    }

}
