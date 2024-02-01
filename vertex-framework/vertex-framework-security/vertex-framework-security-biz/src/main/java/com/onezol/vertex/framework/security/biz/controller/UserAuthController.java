package com.onezol.vertex.framework.security.biz.controller;

import com.onezol.vertex.framework.common.constant.enums.BizHttpStatus;
import com.onezol.vertex.framework.common.helper.ResponseHelper;
import com.onezol.vertex.framework.common.model.pojo.ResponseModel;
import com.onezol.vertex.framework.common.util.StringUtils;
import com.onezol.vertex.framework.security.api.annotation.RestrictAccess;
import com.onezol.vertex.framework.security.api.model.payload.UserLoginPayload;
import com.onezol.vertex.framework.security.api.model.payload.UserRegistrationPayload;
import com.onezol.vertex.framework.security.api.model.vo.UserAuthenticationVO;
import com.onezol.vertex.framework.security.api.service.UserAuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

@Tag(name = "用户授权")
@Validated
@RestController
@RequestMapping("/auth")
public class UserAuthController {

    private final UserAuthService userAuthService;

    public UserAuthController(UserAuthService userAuthService) {
        this.userAuthService = userAuthService;
    }

    @Operation(summary = "用户注册", description = "用户注册")
    @PostMapping("/register")
    public ResponseModel<UserAuthenticationVO> register(@RequestBody @Valid UserRegistrationPayload payload) {
        return ResponseHelper.buildSuccessfulResponse(userAuthService.register(payload));
    }

    @Operation(summary = "用户登录", description = "用户登录: 支持用户名、邮箱登录")
    @PostMapping("/login")
    public ResponseModel<UserAuthenticationVO> login(@RequestBody @Valid UserLoginPayload payload) {
        UserAuthenticationVO userAuthenticationVO = null;
        if (StringUtils.isNotBlank(payload.getUsername())) {
            userAuthenticationVO = userAuthService.loginByUsername(payload.getUsername(), payload.getPassword(), payload.getVerifyCode());
        }
        if (StringUtils.isNotBlank(payload.getEmail())) {
            userAuthenticationVO = userAuthService.loginByEmail(payload.getEmail(), payload.getVerifyCode());
        }
        return Objects.nonNull(userAuthenticationVO) ?
                ResponseHelper.buildSuccessfulResponse(userAuthenticationVO) :
                ResponseHelper.buildFailedResponse(BizHttpStatus.BAD_REQUEST, "请使用用户名或邮箱登录");
    }

    @Operation(summary = "用户登出", description = "用户登出")
    @RestrictAccess
    @PostMapping("/logout")
    public ResponseModel<Void> logout() {
        userAuthService.logout();
        return ResponseHelper.buildSuccessfulResponse();
    }

}
