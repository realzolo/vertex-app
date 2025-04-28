package com.onezol.vertex.framework.security.biz.authentication.provider;

import com.onezol.vertex.framework.common.constant.CacheKey;
import com.onezol.vertex.framework.common.exception.InvalidParameterException;
import com.onezol.vertex.framework.security.biz.authentication.token.EmailAuthenticationToken;
import com.onezol.vertex.framework.support.cache.RedisCache;
import com.onezol.vertex.framework.support.support.RedisKeyHelper;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

@Component
public class EmailAuthenticationProvider implements AuthenticationProvider {

    private final RedisCache redisCache;
    private final UserDetailsService userDetailsService;

    public EmailAuthenticationProvider(RedisCache redisCache, UserDetailsService userDetailsService) {
        this.redisCache = redisCache;
        this.userDetailsService = userDetailsService;
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

        // 校验用户
        UserDetails userDetails = userDetailsService.loadUserByUsername(email);
        if (userDetails == null) {
            throw new BadCredentialsException("用户不存在");
        }

        // 删除验证码
        redisCache.deleteObject(vcRedisKey);

        return new EmailAuthenticationToken(userDetails, code, userDetails.getAuthorities());
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return EmailAuthenticationToken.class.isAssignableFrom(authentication);
    }

}
