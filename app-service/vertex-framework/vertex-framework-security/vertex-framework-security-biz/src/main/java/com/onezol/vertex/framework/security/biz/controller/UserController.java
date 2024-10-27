package com.onezol.vertex.framework.security.biz.controller;

import com.onezol.vertex.framework.common.bean.AuthenticationContext;
import com.onezol.vertex.framework.common.helper.ResponseHelper;
import com.onezol.vertex.framework.common.model.AuthUser;
import com.onezol.vertex.framework.common.model.GenericResponse;
import com.onezol.vertex.framework.security.api.annotation.RestrictAccess;
import com.onezol.vertex.framework.security.api.model.dto.User;
import com.onezol.vertex.framework.security.api.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "用户信息")
@Validated
@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @Operation(summary = "获取当前用户信息", description = "获取当前登录用户信息")
    @RestrictAccess
    @GetMapping("/me")
    public GenericResponse<User> me() {
        AuthUser authUser = AuthenticationContext.get();
        String username = authUser.getUsername();
        return ResponseHelper.buildSuccessfulResponse(userService.queryUserInfo(username));
    }

}
