package com.onezol.vertex.framework.security.biz.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.onezol.vertex.framework.common.model.GenericResponse;
import com.onezol.vertex.framework.common.model.PagePack;
import com.onezol.vertex.framework.common.mvc.controller.BaseController;
import com.onezol.vertex.framework.security.api.model.dto.LoginUser;
import com.onezol.vertex.framework.security.api.model.entity.LoginHistoryEntity;
import com.onezol.vertex.framework.security.api.service.LoginHistoryService;
import com.onezol.vertex.framework.support.support.ResponseHelper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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
    public GenericResponse<PagePack<LoginUser>> getLoginUserPage(@RequestParam("pageNumber") Long pageNumber, @RequestParam("pageSize") Long pageSize) {
        Page<LoginHistoryEntity> page = this.getPageObject(pageNumber, pageSize);
        PagePack<LoginUser> loginUserPage = loginHistoryService.getLoginHistoryPage(page);
        return ResponseHelper.buildSuccessfulResponse(loginUserPage);
    }

    @Operation(summary = "获取登录用户详情")
    @GetMapping("/{id}")
    public GenericResponse<LoginUser> getLoginUser(@PathVariable("id") Long id) {
        LoginUser loginUser = loginHistoryService.getLoginHistoryById(id);
        return ResponseHelper.buildSuccessfulResponse(loginUser);
    }

}
