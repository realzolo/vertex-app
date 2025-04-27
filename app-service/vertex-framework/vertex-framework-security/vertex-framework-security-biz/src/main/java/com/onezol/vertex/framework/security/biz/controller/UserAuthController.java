package com.onezol.vertex.framework.security.biz.controller;

import com.onezol.vertex.framework.common.model.GenericResponse;
import com.onezol.vertex.framework.security.api.annotation.RestrictAccess;
import com.onezol.vertex.framework.security.api.model.dto.AuthIdentity;
import com.onezol.vertex.framework.security.api.model.payload.UserAccountLoginPayload;
import com.onezol.vertex.framework.security.api.model.payload.UserSavePayload;
import com.onezol.vertex.framework.security.api.service.UserAuthService;
import com.onezol.vertex.framework.support.support.ResponseHelper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

@Tag(name = "用户认证")
@Validated
@RestController
@RequestMapping("/auth")
public class UserAuthController {

    private final UserAuthService userAuthService;

    public UserAuthController(UserAuthService userAuthService) {
        this.userAuthService = userAuthService;
    }

//    @Operation(summary = "用户注册", description = "用户注册")
//    @PostMapping("/register")
//    public GenericResponse<UserAuthenticationVO> register(@RequestBody @Valid UserRegistrationPayload payload) {
//        return ResponseHelper.buildSuccessfulResponse(userAuthService.register(payload));
//    }

    @Operation(summary = "用户登录", description = "用户登录: 根据用户名密码")
    @PostMapping("/login")
    public GenericResponse<AuthIdentity> loginByIdPassword(@RequestBody @Valid UserAccountLoginPayload payload) {
        AuthIdentity userAuthenticationVO = userAuthService.loginByIdPassword(
                payload.getUsername(), payload.getPassword(), payload.getFingerprint(), payload.getVerificationCode()
        );

        return Objects.nonNull(userAuthenticationVO) ?
                ResponseHelper.buildSuccessfulResponse(userAuthenticationVO) :
                ResponseHelper.buildFailedResponse("请使用用户名或邮箱登录");
    }

    @Operation(summary = "用户登出", description = "用户登出")
    @RestrictAccess
    @PostMapping("/logout")
    public GenericResponse<Void> logout() {
        userAuthService.logout();
        return ResponseHelper.buildSuccessfulResponse();
    }

}
