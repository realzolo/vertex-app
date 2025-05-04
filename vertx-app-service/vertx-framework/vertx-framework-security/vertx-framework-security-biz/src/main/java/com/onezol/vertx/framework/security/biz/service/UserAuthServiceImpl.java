package com.onezol.vertx.framework.security.biz.service;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.onezol.vertx.framework.common.constant.CacheKey;
import com.onezol.vertx.framework.common.constant.enumeration.Gender;
import com.onezol.vertx.framework.common.exception.InvalidParameterException;
import com.onezol.vertx.framework.common.mvc.service.BaseServiceImpl;
import com.onezol.vertx.framework.common.util.BeanUtils;
import com.onezol.vertx.framework.security.api.context.AuthenticationContext;
import com.onezol.vertx.framework.security.api.enumeration.LoginType;
import com.onezol.vertx.framework.security.api.model.AuthIdentity;
import com.onezol.vertx.framework.security.api.model.UserIdentity;
import com.onezol.vertx.framework.security.api.model.dto.User;
import com.onezol.vertx.framework.security.api.model.dto.UserPassword;
import com.onezol.vertx.framework.security.api.model.entity.UserEntity;
import com.onezol.vertx.framework.security.api.model.payload.UserSavePayload;
import com.onezol.vertx.framework.security.api.service.LoginHistoryService;
import com.onezol.vertx.framework.security.api.service.LoginUserService;
import com.onezol.vertx.framework.security.api.service.UserAuthService;
import com.onezol.vertx.framework.security.biz.mapper.UserMapper;
import com.onezol.vertx.framework.security.biz.strategy.LoginStrategy;
import com.onezol.vertx.framework.security.biz.strategy.LoginStrategyFactory;
import com.onezol.vertx.framework.support.cache.RedisCache;
import com.onezol.vertx.framework.support.support.JwtHelper;
import com.onezol.vertx.framework.support.support.RedisKeyHelper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Objects;

@Service
public class UserAuthServiceImpl extends BaseServiceImpl<UserMapper, UserEntity> implements UserAuthService {

    private final ApplicationContext applicationContext;
    private final PasswordEncoder passwordEncoder;
    private final RedisCache redisCache;
    private final LoginUserService loginUserService;
    private final LoginHistoryService loginHistoryService;

    @Value("${spring.jwt.expiration-time:3600}")
    private Integer expirationTime;

    public UserAuthServiceImpl(ApplicationContext applicationContext, PasswordEncoder passwordEncoder, RedisCache redisCache, LoginUserService loginUserService, LoginHistoryService loginHistoryService) {
        this.applicationContext = applicationContext;
        this.passwordEncoder = passwordEncoder;
        this.redisCache = redisCache;
        this.loginUserService = loginUserService;
        this.loginHistoryService = loginHistoryService;
    }

    /**
     * 用户注册
     *
     * @param payload UserRegistrationPayload
     */
    @Override
    @Transactional
    public AuthIdentity register(UserSavePayload payload) {
//        if (StringUtils.isAnyBlank(payload.getUsername(), payload.getPassword(), payload.getEmail(), payload.getVerifyCode())) {
//            throw new InvalidParameterException("用户名、密码、邮箱、验证码不能为空");
//        }
//
//        // 验证码校验 TODO: Redis验证码校验
//        String verifyCode = payload.getVerifyCode();
////        if (StringUtils.isBlank(verifyCode) || verifyCode.length() != 6 || !verifyCode.equalsIgnoreCase(redisCache.getCacheObject("verifyCode:" + captchaKey))) {
//        if (StringUtils.isBlank(verifyCode) || verifyCode.length() != 6) {
//            throw new InvalidParameterException("验证码错误");
//        }

        UserEntity entity = this.newBlankUser();
        entity.setUsername(payload.getUsername());
        entity.setPassword(passwordEncoder.encode(payload.getPassword()));
        entity.setEmail(payload.getEmail());

        // 唯一性校验
        UserEntity entityFromDB = this.getOne(
                Wrappers.<UserEntity>lambdaQuery()
                        .eq(UserEntity::getUsername, payload.getUsername())
                        .or()
                        .eq(UserEntity::getEmail, entity.getEmail())
        );
        if (Objects.nonNull(entityFromDB)) {
            if (Objects.equals(entityFromDB.getUsername(), entity.getUsername())) {
                throw new InvalidParameterException("用户名已存在");
            } else if (Objects.equals(entityFromDB.getEmail(), entity.getEmail())) {
                throw new InvalidParameterException("邮箱已存在");
            }
        }

        // 保存用户信息
        this.save(entity);

        // 构建返回结果
        User user = BeanUtils.toBean(entity, User.class);
        String token = JwtHelper.createJwt(String.valueOf(user.getId()));
        return AuthIdentity.builder()
                .user(user)
                .jwt(
                        AuthIdentity.Ticket.builder()
                                .token(token)
                                .expire(Long.valueOf(expirationTime))
                                .build()
                )
                .build();
    }

    /**
     * 用户登录
     *
     * @param loginType 登录类型
     * @param params    登录参数
     * @return 认证身份信息
     */
    @Override
    public AuthIdentity login(LoginType loginType, Object... params) {
        Class<? extends LoginStrategy> strategyClass = LoginStrategyFactory.getStrategy(loginType);
        LoginStrategy loginStrategy = applicationContext.getBean(strategyClass);
        return loginStrategy.login(params);
    }


    /**
     * 用户登出
     */
    @Override
    public void logout() {
        UserIdentity userIdentity = AuthenticationContext.get();
        // 移除用户Token
        String tokenKey = RedisKeyHelper.buildCacheKey(CacheKey.USER_TOKEN, String.valueOf(userIdentity.getUserId()));
        redisCache.deleteObject(tokenKey);
        // 移除用户信息
        String infoKey = RedisKeyHelper.buildCacheKey(CacheKey.USER_INFO, String.valueOf(userIdentity.getUserId()));
        redisCache.deleteObject(infoKey);
    }

    /**
     * 获取用户密码信息
     *
     * @param userId 用户ID
     */
    @Override
    public UserPassword getPassword(Long userId) {
        UserEntity entity = this.getById(userId);
        return new UserPassword(entity.getPassword(), entity.getPwdExpDate());
    }

    /**
     * 创建一个空白的用户
     *
     * @return UserEntity
     */
    private UserEntity newBlankUser() {
        UserEntity entity = new UserEntity();
        entity.setUsername("");
        entity.setPassword("");
        entity.setNickname("");
        entity.setName("张三");
        entity.setDescription("");
        entity.setAvatar("");
        entity.setGender(Gender.MALE);
        entity.setBirthday(LocalDate.now());
        entity.setPhone("");
        entity.setEmail("");
        entity.setPwdExpDate(LocalDate.now().plusMonths(3));
        return entity;
    }

}
