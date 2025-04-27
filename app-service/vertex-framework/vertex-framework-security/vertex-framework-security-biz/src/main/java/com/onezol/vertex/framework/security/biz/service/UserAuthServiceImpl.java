package com.onezol.vertex.framework.security.biz.service;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.onezol.vertex.framework.common.constant.CacheKey;
import com.onezol.vertex.framework.common.constant.enumeration.Gender;
import com.onezol.vertex.framework.common.exception.InvalidParameterException;
import com.onezol.vertex.framework.common.exception.RuntimeServiceException;
import com.onezol.vertex.framework.common.mvc.service.BaseServiceImpl;
import com.onezol.vertex.framework.common.util.BeanUtils;
import com.onezol.vertex.framework.common.util.StringUtils;
import com.onezol.vertex.framework.security.api.context.AuthenticationContext;
import com.onezol.vertex.framework.security.api.enumeration.LoginType;
import com.onezol.vertex.framework.security.api.model.LoginUserDetails;
import com.onezol.vertex.framework.security.api.model.dto.AuthIdentity;
import com.onezol.vertex.framework.security.api.model.dto.AuthUser;
import com.onezol.vertex.framework.security.api.model.dto.User;
import com.onezol.vertex.framework.security.api.model.dto.UserPassword;
import com.onezol.vertex.framework.security.api.model.entity.UserEntity;
import com.onezol.vertex.framework.security.api.model.payload.UserSavePayload;
import com.onezol.vertex.framework.security.api.service.LoginHistoryService;
import com.onezol.vertex.framework.security.api.service.LoginUserService;
import com.onezol.vertex.framework.security.api.service.UserAuthService;
import com.onezol.vertex.framework.security.biz.mapper.UserMapper;
import com.onezol.vertex.framework.support.cache.RedisCache;
import com.onezol.vertex.framework.support.support.JWTHelper;
import com.onezol.vertex.framework.support.support.RedisKeyHelper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Objects;

@Service
public class UserAuthServiceImpl extends BaseServiceImpl<UserMapper, UserEntity> implements UserAuthService {

    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final RedisCache redisCache;
    private final LoginUserService loginUserService;
    private final LoginHistoryService loginHistoryService;

    @Value("${spring.jwt.expiration-time:3600}")
    private Integer expirationTime;

    public UserAuthServiceImpl(AuthenticationManager authenticationManager, PasswordEncoder passwordEncoder, RedisCache redisCache, LoginUserService loginUserService, LoginHistoryService loginHistoryService) {
        this.authenticationManager = authenticationManager;
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
        String token = JWTHelper.generateToken(String.valueOf(user.getId()));
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
     * 用户登录(用户名密码登录)
     *
     * @param username         用户名
     * @param password         密码
     * @param fingerprint      用户指纹
     * @param verificationCode 验证码
     */
    @Override
    public AuthIdentity loginByIdPassword(String username, String password, String fingerprint, String verificationCode) throws RuntimeServiceException {
        if (StringUtils.isBlank(fingerprint)) {
            throw new InvalidParameterException("用户指纹不能为空");
        }
        if (StringUtils.isAnyBlank(username, password)) {
            throw new InvalidParameterException("用户名或密码不能为空");
        }

        // 校验验证码
        String captchaRedisKey = RedisKeyHelper.buildCacheKey(CacheKey.VC_UP, fingerprint);
        String verificationCodeInRedis = redisCache.getCacheObject(captchaRedisKey);
        if (!verificationCode.equalsIgnoreCase(verificationCodeInRedis)) {
            throw new InvalidParameterException("验证码错误");
        }

        // 调用 SpringSecurity 的 AuthenticationManager 处理登录验证
        Authentication authentication = new UsernamePasswordAuthenticationToken(username, password);
        try {
            authentication = authenticationManager.authenticate(authentication);
        } catch (AuthenticationException ex) {
            String message = switch (ex.getClass().getSimpleName()) {
                case "BadCredentialsException" -> "用户名或密码错误";
                case "CredentialsExpiredException" -> "密码已过期";
                case "DisabledException" -> "账号已被禁用";
                case "LockedException" -> "账号已被锁定";
                default -> ex.getMessage();
            };
            // 登录失败时移除Redis中的验证码
            redisCache.deleteObject(captchaRedisKey);
            throw new RuntimeServiceException(message);
        }

        // 获取用户信息
        Object principal = authentication.getPrincipal();
        LoginUserDetails loginUserDetails = (LoginUserDetails) principal;

        // 处理登录成功后的逻辑
        return afterLoginSuccess(loginUserDetails, LoginType.UP);
    }

    /**
     * 用户登录(根据邮箱)
     *
     * @param email      电子邮箱
     * @param verifyCode 验证码
     */
    @Override
    public AuthIdentity loginByEmail(String email, String verifyCode) {
        return null;
    }

    /**
     * 用户登出
     */
    @Override
    public void logout() {
        AuthUser authUser = AuthenticationContext.get();
        // 移除用户Token
        String tokenKey = RedisKeyHelper.buildCacheKey(CacheKey.USER_TOKEN, String.valueOf(authUser.getUserId()));
        redisCache.deleteObject(tokenKey);
        // 移除用户信息
        String infoKey = RedisKeyHelper.buildCacheKey(CacheKey.USER_INFO, String.valueOf(authUser.getUserId()));
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
     * 登录成功后的处理
     *
     * @param loginUserDetails 登录用户身份信息
     * @return 登录成功后的处理结果
     */
    private AuthIdentity afterLoginSuccess(LoginUserDetails loginUserDetails, final LoginType loginType) {
        // 生成token
        String token = JWTHelper.generateToken(loginUserDetails.getId().toString());

        // 缓存用户数据
        loginUserService.addLoginUser(loginUserDetails, token);

        // 存储登录日志
        loginHistoryService.createLoginRecord(loginUserDetails, loginType);

        // 返回结果
        return AuthIdentity.builder()
                .user(loginUserDetails)
                .jwt(
                        AuthIdentity.Ticket.builder()
                                .token(token)
                                .expire(Long.valueOf(expirationTime))
                                .build()
                )
                .build();
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
