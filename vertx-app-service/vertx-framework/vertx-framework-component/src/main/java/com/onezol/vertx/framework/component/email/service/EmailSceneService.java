package com.onezol.vertx.framework.component.email.service;

import com.onezol.vertx.framework.common.util.DateUtils;
import com.onezol.vertx.framework.component.email.constant.MailTemplate;
import com.onezol.vertx.framework.component.email.model.LoginVerificationCodeTemplateVariables;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
public class EmailSceneService {

    @Value("${application.name}")
    private String applicationName;

    private final EmailBasicService emailBasicService;

    public EmailSceneService(EmailBasicService emailBasicService) {
        this.emailBasicService = emailBasicService;
    }

    /**
     * 发送邮件：登录验证码
     *
     * @param targetMail       目标邮箱
     * @param verificationCode 验证码
     */
    public void sendLoginVerificationCode(String targetMail, String verificationCode) {
        LoginVerificationCodeTemplateVariables variables = new LoginVerificationCodeTemplateVariables();
        variables.setApplicationName(applicationName);
        variables.setVerificationCode(verificationCode);
        variables.setVerificationCodeExpire(5);
        variables.setSendTime(DateUtils.format(LocalDateTime.now(), "yyyy-MM-dd HH:mm:ss"));
        List<String> targetMails = List.of(targetMail);
        this.emailBasicService.sendHtmlTemplate(MailTemplate.LOGIN_VERIFICATION_CODE.getName(), MailTemplate.LOGIN_VERIFICATION_CODE, variables, targetMails);
    }

}
