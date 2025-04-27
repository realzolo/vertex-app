package com.onezol.vertex.framework.security.biz.controller;

import cn.hutool.core.util.RandomUtil;
import com.onezol.vertex.framework.common.constant.CacheKey;
import com.onezol.vertex.framework.common.model.GenericResponse;
import com.onezol.vertex.framework.common.util.ValidationUtils;
import com.onezol.vertex.framework.component.email.service.EmailSceneService;
import com.onezol.vertex.framework.support.cache.RedisCache;
import com.onezol.vertex.framework.support.support.RedisKeyHelper;
import com.onezol.vertex.framework.support.support.ResponseHelper;
import com.wf.captcha.GifCaptcha;
import com.wf.captcha.base.Captcha;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.concurrent.TimeUnit;

@Tag(name = "验证码")
@Slf4j
@Validated
@RestController
@RequestMapping("/vc")
public class CaptchaController {

    private final RedisCache redisCache;
    private final EmailSceneService emailSceneService;

    public CaptchaController(RedisCache redisCache, EmailSceneService emailSceneService) {
        this.redisCache = redisCache;
        this.emailSceneService = emailSceneService;
    }

    @Operation(summary = "获取数字验证码", description = "获取数字验证码图片")
    @GetMapping("/digit-image")
    public GenericResponse<Map<String, Object>> getDigitalImageCaptcha(@RequestParam("fingerprint") String fingerprint) {
        // 生成验证码
        Captcha captcha = new GifCaptcha(111, 36, 4);

        // 存储到Redis
        String cacheKey = RedisKeyHelper.buildCacheKey(CacheKey.VC_UP, fingerprint);
        redisCache.setCacheObject(cacheKey, captcha.text(), 60, TimeUnit.SECONDS);

        long expireTime = System.currentTimeMillis() + 60 * 1000;
        Map<String, Object> result = Map.of(
                "fingerprint", fingerprint,
                "image", captcha.toBase64(),
                "expireTime", expireTime
        );

        return ResponseHelper.buildSuccessfulResponse(result);
    }

    @Operation(summary = "发送邮箱验证码", description = "发送邮箱登录验证码")
    @GetMapping("/email")
    public GenericResponse<Void> sendEmailLoginVerificationCode(@RequestParam("email") String email) {
        if (!ValidationUtils.validateEmail(email)) {
            return ResponseHelper.buildFailedResponse("邮箱格式不正确");
        }

        // 生成验证码
        String verificationCode = RandomUtil.randomNumbers(6);

        // 存储到Redis
        String cacheKey = RedisKeyHelper.buildCacheKey(CacheKey.VC_EMAIL, email.toLowerCase());
        redisCache.setCacheObject(cacheKey, verificationCode, 60 * 5, TimeUnit.SECONDS);

        // 发送验证码
        try {
            emailSceneService.sendLoginVerificationCode(email, verificationCode);
        } catch (Exception e) {
            redisCache.deleteObject(cacheKey);
            log.error("[验证码] 邮箱验证码发送失败", e);
            return ResponseHelper.buildFailedResponse("验证码发送失败");
        }

        return ResponseHelper.buildSuccessfulResponse();
    }

}
