package com.onezol.vertx.framework.security.biz.authentication.provider;

import com.onezol.vertx.framework.common.constant.CacheKey;
import com.onezol.vertx.framework.common.exception.InvalidParameterException;
import com.onezol.vertx.framework.security.biz.authentication.token.EmailAuthenticationToken;
import com.onezol.vertx.framework.support.cache.RedisCache;
import com.onezol.vertx.framework.support.support.RedisKeyHelper;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

@Component
public class EmailAuthenticationProvider implements AuthenticationProvider {

    private final UserDetailsService userDetailsService;
    private final RedisCache redisCache;

    public EmailAuthenticationProvider(UserDetailsService userDetailsService, RedisCache redisCache) {
        this.userDetailsService = userDetailsService;
        this.redisCache = redisCache;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String email = (String) authentication.getPrincipal();
        String code = (String) authentication.getCredentials();

        // 校验验证码
        String vcRedisKey = RedisKeyHelper.buildCacheKey(CacheKey.VC_EMAIL, email.toLowerCase());
        String verificationCodeInRedis = redisCache.getCacheObject(vcRedisKey);
        if (!code.equalsIgnoreCase(verificationCodeInRedis)) {
            throw new InvalidParameterException("验证码错误或已过期");
        }
        redisCache.deleteObject(vcRedisKey);

        // 校验用户
        UserDetails userDetails = userDetailsService.loadUserByUsername(email);
        if (userDetails == null) {
            throw new BadCredentialsException("当前邮箱未绑定账号");
        }

        return new EmailAuthenticationToken(userDetails, code, userDetails.getAuthorities());
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return EmailAuthenticationToken.class.isAssignableFrom(authentication);
    }

}
