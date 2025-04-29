package com.onezol.vertx.framework.security.biz.strategy.core;

import com.onezol.vertx.framework.common.constant.CacheKey;
import com.onezol.vertx.framework.common.exception.InvalidParameterException;
import com.onezol.vertx.framework.common.util.StringUtils;
import com.onezol.vertx.framework.security.api.enumeration.LoginType;
import com.onezol.vertx.framework.security.api.model.AuthIdentity;
import com.onezol.vertx.framework.security.api.model.LoginUserDetails;
import com.onezol.vertx.framework.security.biz.strategy.LoginStrategy;
import com.onezol.vertx.framework.security.biz.strategy.LoginSupport;
import com.onezol.vertx.framework.support.cache.RedisCache;
import com.onezol.vertx.framework.support.support.RedisKeyHelper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

@Component
public class UsernamePasswordAuthenticationStrategy implements LoginStrategy {

    private final AuthenticationManager authenticationManager;
    private final RedisCache redisCache;
    private final LoginSupport loginSupport;

    public UsernamePasswordAuthenticationStrategy(AuthenticationManager authenticationManager, RedisCache redisCache, LoginSupport loginSupport) {
        this.authenticationManager = authenticationManager;
        this.redisCache = redisCache;
        this.loginSupport = loginSupport;
    }

    @Override
    public AuthIdentity login(Object... params) throws AuthenticationException {
        if (params.length < 4) {
            throw new InvalidParameterException("执行登录失败，参数异常。请检查参数列表：username, password, fingerprint, verificationCode");
        }

        String username = (String) params[0];
        String password = (String) params[1];
        String fingerprint = (String) params[2];
        String verificationCode = (String) params[3];
        if (StringUtils.isBlank(fingerprint)) throw new InvalidParameterException("用户指纹不能为空");
        if (StringUtils.isAnyBlank(username, password)) throw new InvalidParameterException("用户名或密码不能为空");

        // 校验验证码
        String vcRedisKey = RedisKeyHelper.buildCacheKey(CacheKey.VC_UP, fingerprint);
        String verificationCodeInRedis = redisCache.getCacheObject(vcRedisKey);
        if (!verificationCode.equalsIgnoreCase(verificationCodeInRedis)) {
            throw new InvalidParameterException("验证码错误");
        }
        redisCache.deleteObject(vcRedisKey);

        // 执行认证
        Authentication authentication = createAuthentication(params);
        authentication = authenticationManager.authenticate(authentication);

        // 获取用户信息
        Object principal = authentication.getPrincipal();
        LoginUserDetails loginUserDetails = (LoginUserDetails) principal;

        // 处理登录成功后的逻辑
        return loginSupport.afterLoginSuccess(loginUserDetails, LoginType.UP);
    }

    @Override
    public Authentication createAuthentication(Object... params) {
        String username = (String) params[0];
        String password = (String) params[1];
        return new UsernamePasswordAuthenticationToken(username, password);
    }

}
