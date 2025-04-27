package com.onezol.vertex.framework.component.email.service;

import com.onezol.vertex.framework.common.util.ResourceUtils;
import com.onezol.vertex.framework.common.util.TemplateUtils;
import com.onezol.vertex.framework.component.configuration.service.RuntimeConfigurationService;
import com.onezol.vertex.framework.component.email.constant.MailTemplate;
import jakarta.annotation.PostConstruct;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.Properties;

@Slf4j
@Service
public class EmailBasicService {

    private final ApplicationContext applicationContext;
    private final RuntimeConfigurationService runtimeConfigurationService;
    private JavaMailSender javaMailSender;
    private Properties mailProperties;

    public EmailBasicService(ApplicationContext applicationContext, RuntimeConfigurationService runtimeConfigurationService) {
        this.applicationContext = applicationContext;
        this.runtimeConfigurationService = runtimeConfigurationService;
    }

    @PostConstruct
    public void postConstruct() {
        this.initialize();
    }

    /**
     * 发送文本邮件
     *
     * @param title       标题
     * @param text        文本
     * @param ccMails     抄送人
     * @param targetMails 目标邮箱
     */
    public void sendText(String title, String text, List<String> ccMails, List<String> targetMails) {
        this.preCheck();

        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(mailProperties.getProperty("MAIL_USERNAME"));
        message.setTo(targetMails.toArray(new String[0]));
        message.setSubject(title);
        message.setText(text);
        if (ccMails != null && !ccMails.isEmpty()) {
            message.setCc(ccMails.toArray(new String[0]));
        }

        javaMailSender.send(message);
    }

    /**
     * 发送HTML超文本邮件
     *
     * @param title          标题
     * @param mailTemplate   模版
     * @param variableObject 变量对象
     * @param targetMails    目标邮箱
     */
    public void sendHtmlTemplate(String title, MailTemplate mailTemplate, Object variableObject, List<String> targetMails) {
        String html;
        try {
            html = ResourceUtils.readClassPathResourceAsText(mailTemplate.getValue());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        html = TemplateUtils.processWithSpEL(html, variableObject);
        this.sendHtmlText(title, html, null, targetMails);
    }

    /**
     * 发送HTML超文本邮件
     *
     * @param title       标题
     * @param html        HTML超文本
     * @param ccMails     抄送人
     * @param targetMails 目标邮箱
     */
    @SneakyThrows(MessagingException.class)
    public void sendHtmlText(String title, String html, List<String> ccMails, List<String> targetMails) {
        this.preCheck();

        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper mimeHelper = new MimeMessageHelper(message, true, "UTF-8");

        message.setFrom(mailProperties.getProperty("MAIL_USERNAME"));
        mimeHelper.setTo(targetMails.toArray(new String[0]));
        mimeHelper.setSubject(title);
        mimeHelper.setText(html, true);
        if (ccMails != null && !ccMails.isEmpty()) {
            mimeHelper.setCc(ccMails.toArray(new String[0]));
        }

        javaMailSender.send(message);
    }


    /**
     * 初始化邮件发送器
     */
    private void initialize() {
        JavaMailSenderImpl javaMailSender = new JavaMailSenderImpl();
        javaMailSender.setDefaultEncoding("UTF-8");
        Properties props = javaMailSender.getJavaMailProperties();

        Properties properties = runtimeConfigurationService.getConfiguration("MAIL");
        if (properties.isEmpty()) return;

        javaMailSender.setHost(properties.getProperty("MAIL_HOST"));
        javaMailSender.setPort(Integer.parseInt(properties.getProperty("MAIL_PORT")));
        javaMailSender.setProtocol(properties.getProperty("MAIL_PROTOCOL"));
        javaMailSender.setUsername(properties.getProperty("MAIL_USERNAME"));
        javaMailSender.setPassword(properties.getProperty("MAIL_PASSWORD"));
        props.put("mail.smtp.ssl.enable", Objects.equals(properties.getProperty("MAIL_SSL_ENABLED"), "1") ? "true" : "false");
        props.put("mail.smtp.ssl.port", properties.getProperty("MAIL_SSL_PORT"));

        DefaultListableBeanFactory beanFactory = (DefaultListableBeanFactory) applicationContext.getAutowireCapableBeanFactory();
        beanFactory.registerSingleton("javaMailSender", javaMailSender);

        this.javaMailSender = javaMailSender;
        this.mailProperties = properties;

        log.info("[邮件] 配置加载完成");
    }

    /**
     * 预检查，检查邮件服务是否正常启动
     */
    private void preCheck() {
        if (javaMailSender == null) {
            throw new IllegalStateException("请检查邮件服务是否正常启动！");
        }
    }

}
