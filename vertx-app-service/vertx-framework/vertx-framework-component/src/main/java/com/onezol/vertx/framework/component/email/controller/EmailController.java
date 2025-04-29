package com.onezol.vertx.framework.component.email.controller;

import com.onezol.vertx.framework.component.email.service.EmailBasicService;
import com.onezol.vertx.framework.component.email.service.EmailSceneService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@Tag(name = "邮件")
@RestController
@RequestMapping("/email")
public class EmailController {

    private final EmailBasicService emailBasicService;
    private final EmailSceneService emailSceneService;

    public EmailController(EmailBasicService emailBasicService, EmailSceneService emailSceneService) {
        this.emailBasicService = emailBasicService;
        this.emailSceneService = emailSceneService;
    }

    @PostMapping("/send/login-verification-code")
    public String sendTemplateMail() throws Exception {
        Map<String, Object> variables = new HashMap<>();
        variables.put("title", "模板邮件标题");
        variables.put("username", "张三");
        variables.put("content", "这是通过模板生成的邮件内容");

        emailSceneService.sendLoginVerificationCode("zolo@onezol.com", "123456");
        return "模板邮件发送成功";
    }
}