package com.onezol.vertex.framework.security.biz.service;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.onezol.vertex.framework.common.bean.AuthenticationContext;
import com.onezol.vertex.framework.common.constant.RedisKey;
import com.onezol.vertex.framework.common.constant.enums.UserGender;
import com.onezol.vertex.framework.common.exception.RuntimeBizException;
import com.onezol.vertex.framework.common.model.AuthUser;
import com.onezol.vertex.framework.common.mvc.service.BaseServiceImpl;
import com.onezol.vertex.framework.common.util.*;
import com.onezol.vertex.framework.security.api.mapper.UserMapper;
import com.onezol.vertex.framework.security.api.model.dto.User;
import com.onezol.vertex.framework.security.api.model.entity.UserEntity;
import com.onezol.vertex.framework.security.api.model.payload.UserRegistrationPayload;
import com.onezol.vertex.framework.security.api.model.pojo.LoginUser;
import com.onezol.vertex.framework.security.api.model.vo.UserAuthenticationVO;
import com.onezol.vertex.framework.security.api.service.OnlineUserService;
import com.onezol.vertex.framework.security.api.service.UserAuthService;
import com.onezol.vertex.framework.support.cache.RedisCache;
import com.onezol.vertex.framework.support.support.RedisKeyHelper;
import eu.bitwalker.useragentutils.UserAgent;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

@Service
public class UserAuthServiceImpl extends BaseServiceImpl<UserMapper, UserEntity> implements UserAuthService {

    private final AuthenticationManager authenticationManager;

    private final OnlineUserService onlineUserService;

    private final PasswordEncoder passwordEncoder;

    private final RedisCache redisCache;

    @Value("${spring.jwt.expiration-time:3600}")
    private Integer expirationTime;

    public UserAuthServiceImpl(AuthenticationManager authenticationManager, OnlineUserService onlineUserService, PasswordEncoder passwordEncoder, RedisCache redisCache) {
        this.authenticationManager = authenticationManager;
        this.onlineUserService = onlineUserService;
        this.passwordEncoder = passwordEncoder;
        this.redisCache = redisCache;
    }

    /**
     * 根据用户名获取用户信息
     *
     * @param username 用户名
     * @return 用户信息
     */
    public UserEntity getUserByUsername(String username) {
        if (StringUtils.isBlank(username)) {
            return null;
        }
        return this.getOne(
                Wrappers.<UserEntity>lambdaQuery()
                        .eq(UserEntity::getUsername, username)
        );
    }

    /**
     * 用户注册
     *
     * @param payload UserRegistrationPayload
     */
    @Override
    @Transactional
    public UserAuthenticationVO register(UserRegistrationPayload payload) {
        if (StringUtils.isAnyBlank(payload.getUsername(), payload.getPassword(), payload.getEmail(), payload.getVerifyCode())) {
            throw new RuntimeBizException("用户名、密码、邮箱、验证码不能为空");
        }

        // 验证码校验 TODO: Redis验证码校验
        String verifyCode = payload.getVerifyCode();
//        if (StringUtils.isBlank(verifyCode) || verifyCode.length() != 6 || !verifyCode.equalsIgnoreCase(redisCache.getCacheObject("verifyCode:" + captchaKey))) {
        if (StringUtils.isBlank(verifyCode) || verifyCode.length() != 6) {
            throw new RuntimeBizException("验证码错误");
        }

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
                throw new RuntimeBizException("用户名已存在");
            } else if (Objects.equals(entityFromDB.getEmail(), entity.getEmail())) {
                throw new RuntimeBizException("邮箱已存在");
            }
        }

        // 保存用户信息
        this.save(entity);

        // 构建返回结果
        User user = BeanUtils.toBean(entity, User.class);
        String token = JwtUtils.generateToken(CodecUtils.encodeBase64(String.valueOf(user.getId())));
        return UserAuthenticationVO.builder()
                .user(user)
                .jwt(
                        UserAuthenticationVO.UserAuthenticationJWT.builder()
                                .token(token)
                                .expire(Long.valueOf(expirationTime))
                                .build()
                )
                .build();
    }

    /**
     * 用户登录
     *
     * @param username 用户名
     * @param password 密码
     * @param captcha  验证码
     */
    @Override
    public UserAuthenticationVO loginByIdPassword(String username, String password, String sessionId, String captcha) throws RuntimeBizException {
        // 参数校验
        if (StringUtils.isBlank(sessionId)) {
            throw new RuntimeBizException("会话ID不能为空");
        }
        if (StringUtils.isAnyBlank(username, password)) {
            throw new RuntimeBizException("用户名或密码不能为空");
        }

        // 校验验证码
        String captchaRedisKey = RedisKeyHelper.buildRedisKey(RedisKey.CAPTCHA, sessionId);
        String captchaInRedis = redisCache.getCacheObject(captchaRedisKey);
        if (StringUtils.isBlank(captcha) || !captcha.equalsIgnoreCase(captchaInRedis)) {
            throw new RuntimeBizException("验证码错误");
        }

        // 调用SpringSecurity的AuthenticationManager处理登录验证
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
            throw new RuntimeBizException(message);
        }

        // 获取用户信息
        Object principal = authentication.getPrincipal();
        LoginUser loginUser = (LoginUser) principal;

        // 处理登录成功后的逻辑
        return afterLoginSuccess(loginUser);
    }

    /**
     * 用户登录(根据邮箱)
     *
     * @param email      电子邮箱
     * @param verifyCode 验证码
     */
    @Override
    public UserAuthenticationVO loginByEmail(String email, String verifyCode) {
        return null;
    }

    /**
     * 用户登出
     */
    @Override
    public void logout() {
        AuthUser authUser = AuthenticationContext.get();
        // 移除用户Token
        String tokenKey = RedisKeyHelper.buildRedisKey(RedisKey.USER_TOKEN, String.valueOf(authUser.getUserId()));
        redisCache.deleteObject(tokenKey);
        // 移除用户信息
        String infoKey = RedisKeyHelper.buildRedisKey(RedisKey.USER_INFO, String.valueOf(authUser.getUserId()));
        redisCache.deleteObject(infoKey);
    }

    /**
     * 登录成功后的处理
     *
     * @param loginUser 登录用户身份信息
     * @return 登录成功后的处理结果
     */
    private UserAuthenticationVO afterLoginSuccess(final LoginUser loginUser) {
        // 设置用户登录信息
        this.setLoginUserDetails(loginUser);

        // 生成token
        String subject = String.valueOf(loginUser.getDetails().getId());
        String token = JwtUtils.generateToken(subject);

        // Redis存储用户Token
        String tokenKey = RedisKeyHelper.buildRedisKey(RedisKey.USER_TOKEN, subject);
        redisCache.setCacheObject(tokenKey, token, expirationTime, TimeUnit.SECONDS);

        // Redis存储用户信息
        onlineUserService.addOnlineUser(loginUser);

        // 返回结果
        User user = loginUser.getDetails();
        return UserAuthenticationVO.builder()
                .user(user)
                .jwt(
                        UserAuthenticationVO.UserAuthenticationJWT.builder()
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
        entity.setIntroduction("");
        entity.setAvatar("");
        entity.setGender(UserGender.MALE.getCode());
        entity.setBirthday(LocalDate.now());
        entity.setPhone("");
        entity.setEmail("");
        entity.setPwdExpDate(LocalDate.now().plusMonths(3));
        return entity;
    }

    /**
     * 设置用户登录信息
     *
     * @param user 用户信息
     */
    private void setLoginUserDetails(final LoginUser user) {
        LocalDateTime loginTime = LocalDateTime.now();
        String ip = NetworkUtils.getHostIp();
        String location = NetworkUtils.getAddressByIP(ip);
        UserAgent userAgent = ServletUtils.getUserAgent();
        String browser = userAgent.getBrowser().getName();
        String os = userAgent.getOperatingSystem().getName();

        user.setLoginTime(loginTime);
        user.setIp(ip);
        user.setLocation(location);
        user.setBrowser(browser);
        user.setOs(os);
    }

}
