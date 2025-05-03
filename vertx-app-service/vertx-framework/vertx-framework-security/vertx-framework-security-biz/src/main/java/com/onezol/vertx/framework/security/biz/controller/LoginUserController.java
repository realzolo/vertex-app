package com.onezol.vertx.framework.security.biz.controller;

import com.onezol.vertx.framework.common.model.GenericResponse;
import com.onezol.vertx.framework.common.model.PagePack;
import com.onezol.vertx.framework.security.api.model.dto.LoginUser;
import com.onezol.vertx.framework.security.api.service.LoginUserService;
import com.onezol.vertx.framework.support.support.ResponseHelper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@Tag(name = "登录用户控制器")
@RestController
@RequestMapping("/login-user")
public class LoginUserController {

    private final LoginUserService loginUserService;

    public LoginUserController(LoginUserService loginUserService) {
        this.loginUserService = loginUserService;
    }

    @Operation(summary = "获取登录用户分页列表")
    @GetMapping("/page")
    @PreAuthorize("@Security.hasPermission('monitor:online:list')")
    public GenericResponse<PagePack<LoginUser>> getLoginUserPage(@RequestParam("pageNumber") Long pageNumber, @RequestParam("pageSize") Long pageSize) {
        PagePack<LoginUser> loginUserPage = loginUserService.getLoginUserPage(pageNumber, pageSize);
        return ResponseHelper.buildSuccessfulResponse(loginUserPage);
    }

    @Operation(summary = "强制退出用户")
    @DeleteMapping("/force-logout/{token}")
    @PreAuthorize("@Security.hasPermission('monitor:online:kickout')")
    public GenericResponse<Void> forceLogout(@PathVariable("token") Long token) {
        loginUserService.removeLoginUser(token);
        return ResponseHelper.buildSuccessfulResponse();
    }

}
