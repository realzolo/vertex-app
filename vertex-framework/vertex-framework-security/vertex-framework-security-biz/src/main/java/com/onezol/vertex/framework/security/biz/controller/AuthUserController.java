package com.onezol.vertex.framework.security.biz.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "用户授权管理")
@RestController
@RequestMapping("/user")
public class AuthUserController {

    @Operation(summary = "测试接口", description = "测试描述")
    @GetMapping("/test")
    public String test() {
        return "ok";
    }
}
