package com.onezol.vertex.framework.security.biz.controller;

import com.onezol.vertex.framework.common.constant.RedisKey;
import com.onezol.vertex.framework.common.helper.ResponseHelper;
import com.onezol.vertex.framework.common.model.GenericResponse;
import com.onezol.vertex.framework.support.cache.RedisCache;
import com.onezol.vertex.framework.support.support.RedisKeyHelper;
import com.wf.captcha.GifCaptcha;
import com.wf.captcha.base.Captcha;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Tag(name = "验证码")
@Validated
@RestController
@RequestMapping("/captcha")
public class CaptchaController {
    @Autowired
    private RedisCache redisCache;

    @GetMapping("/image")
    public GenericResponse<Map<String, Object>> getCaptcha() {
        Captcha captcha = new GifCaptcha(111, 36, 4);
        String uuid = UUID.randomUUID().toString();
        String redisKey = RedisKeyHelper.buildRedisKey(RedisKey.CAPTCHA, uuid);
        redisCache.setCacheObject(redisKey, captcha.text(), 60, TimeUnit.SECONDS);
        long expires = TimeUnit.MINUTES.toSeconds(60);

        Map<String, Object> result = Map.of(
                "img", captcha.toBase64(),
                "key", uuid,
                "expires", expires
        );
        return ResponseHelper.buildSuccessfulResponse(result);
    }
}
