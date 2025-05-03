package com.onezol.vertx.framework.security.biz.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.onezol.vertx.framework.common.model.GenericResponse;
import com.onezol.vertx.framework.common.model.PagePack;
import com.onezol.vertx.framework.common.mvc.controller.BaseController;
import com.onezol.vertx.framework.security.api.model.dto.LoginUser;
import com.onezol.vertx.framework.security.api.model.entity.LoginHistoryEntity;
import com.onezol.vertx.framework.security.api.service.LoginHistoryService;
import com.onezol.vertx.framework.support.support.ResponseHelper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@Tag(name = "用户登录记录控制器")
@RestController
@RequestMapping("/login-history")
public class LoginHistoryController extends BaseController<LoginHistoryEntity> {

    private final LoginHistoryService loginHistoryService;

    public LoginHistoryController(LoginHistoryService loginHistoryService) {
        this.loginHistoryService = loginHistoryService;
    }

    @Operation(summary = "获取登录用户分页列表")
    @GetMapping("/page")
    @PreAuthorize("@Security.hasPermission('monitor:log:list')")
    public GenericResponse<PagePack<LoginUser>> getLoginUserPage(@RequestParam("pageNumber") Long pageNumber, @RequestParam("pageSize") Long pageSize) {
        Page<LoginHistoryEntity> page = this.getPageObject(pageNumber, pageSize);
        PagePack<LoginUser> loginUserPage = loginHistoryService.getLoginHistoryPage(page);
        return ResponseHelper.buildSuccessfulResponse(loginUserPage);
    }

    @Operation(summary = "获取登录用户详情")
    @GetMapping("/{id}")
    @PreAuthorize("@Security.hasPermission('monitor:log:detail')")
    public GenericResponse<LoginUser> getLoginUser(@PathVariable("id") Long id) {
        LoginUser loginUser = loginHistoryService.getLoginHistoryById(id);
        return ResponseHelper.buildSuccessfulResponse(loginUser);
    }

}
