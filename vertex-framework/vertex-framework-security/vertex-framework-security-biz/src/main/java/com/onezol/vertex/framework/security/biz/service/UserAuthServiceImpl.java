package com.onezol.vertex.framework.security.biz.service;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.onezol.vertex.framework.common.constant.enums.BizCode;
import com.onezol.vertex.framework.common.constant.enums.UserGender;
import com.onezol.vertex.framework.common.exception.RuntimeBizException;
import com.onezol.vertex.framework.common.helper.CodeGenerationHelper;
import com.onezol.vertex.framework.common.mvc.service.BaseServiceImpl;
import com.onezol.vertex.framework.common.util.BeanUtils;
import com.onezol.vertex.framework.common.util.CodecUtils;
import com.onezol.vertex.framework.common.util.JwtUtils;
import com.onezol.vertex.framework.common.util.StringUtils;
import com.onezol.vertex.framework.security.api.mapper.UserMapper;
import com.onezol.vertex.framework.security.api.model.dto.User;
import com.onezol.vertex.framework.security.api.model.entity.UserEntity;
import com.onezol.vertex.framework.security.api.model.payload.UserRegistrationPayload;
import com.onezol.vertex.framework.security.api.model.pojo.LoginUser;
import com.onezol.vertex.framework.security.api.model.vo.UserAuthenticationVO;
import com.onezol.vertex.framework.security.api.service.UserAuthService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AccountExpiredException;
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

    @Value("${spring.jwt.expiration-time:3600}")
    private Integer expirationTime;

    public UserAuthServiceImpl(AuthenticationManager authenticationManager, PasswordEncoder passwordEncoder) {
        this.authenticationManager = authenticationManager;
        this.passwordEncoder = passwordEncoder;
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
                        .eq(UserEntity::getCode, entity.getCode())
                        .or()
                        .eq(UserEntity::getEmail, entity.getEmail())
        );
        if (Objects.nonNull(entityFromDB)) {
            if (Objects.equals(entityFromDB.getUsername(), entity.getUsername())) {
                throw new RuntimeBizException("用户名已存在");
            } else if (Objects.equals(entityFromDB.getEmail(), entity.getEmail())) {
                throw new RuntimeBizException("邮箱已存在");
            } else {
                entity.setCode(CodeGenerationHelper.generateCode(BizCode.USER_CODE));
            }
        }

        // 保存用户信息
        this.save(entity);


        // 构建返回结果
        User user = BeanUtils.toBean(entity, User.class);
        String token = JwtUtils.generateToken(CodecUtils.encodeBase64(user.getCode() + "@" + user.getUsername()));
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
     * @param username   用户名
     * @param password   密码
     * @param verifyCode 验证码
     */
    @Override
    public UserAuthenticationVO loginByUsername(String username, String password, String verifyCode) throws RuntimeBizException {
        // 参数校验
        if (StringUtils.isAnyBlank(username, password)) {
            throw new RuntimeBizException("用户名或密码不能为空");
        }

        // 验证码校验 TODO: Redis验证码校验
//        if (StringUtils.isBlank(captcha) || captcha.length() != 6 || !captcha.equalsIgnoreCase(redisCache.getCacheObject("verifyCode:" + captchaKey))) {
        if (StringUtils.isBlank(verifyCode) || verifyCode.length() != 6) {
            throw new RuntimeBizException("验证码错误");
        }

        Authentication authentication = new UsernamePasswordAuthenticationToken(username, password);

        // 调用SpringSecurity的AuthenticationManager处理登录验证
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
     * 登录成功后的处理
     *
     * @param userIdentity 用户身份信息
     * @return 登录成功后的处理结果
     */
    private UserAuthenticationVO afterLoginSuccess(final LoginUser userIdentity) {
        // 记录用户登录信息
//        this.recordLoginDetails(userIdentity);

        // 生成token
        String subject = userIdentity.getKey();
        String token = JwtUtils.generateToken(subject);

        // 将用户信息放入缓存
//        onlineUserService.addOnlineUser(userIdentity, expirationTime);

        // 返回结果
        User user = userIdentity.getUser();
//        return new HashMap<>() {{
//            this.put("user", user);
//            this.put("jwt", new HashMap<String, Object>() {{
//                this.put("token", token);
//                this.put("expire", expirationTime);
//            }});
//        }};
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
        entity.setCode(CodeGenerationHelper.generateCode(BizCode.USER_CODE));
        entity.setAgencyCode(10000L);
        entity.setUsername("");
        entity.setPassword("");
        entity.setNickname("普通用户");
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

}
