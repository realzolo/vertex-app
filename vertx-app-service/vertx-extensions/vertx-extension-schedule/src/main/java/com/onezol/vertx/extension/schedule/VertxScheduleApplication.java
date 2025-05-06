package com.onezol.vertx.extension.schedule;

import cn.hutool.core.net.NetUtil;
import cn.hutool.core.util.URLUtil;
import com.aizuda.snailjob.server.SnailJobServerApplication;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.web.ServerProperties;

@SpringBootApplication
public class VertxScheduleApplication extends SnailJobServerApplication implements ApplicationRunner {

    private static final Logger logger = LoggerFactory.getLogger(VertxScheduleApplication.class);

    private final ServerProperties serverProperties;

    public VertxScheduleApplication(ServerProperties serverProperties) {
        this.serverProperties = serverProperties;
    }

    public static void main(String[] args) {
        SpringApplication.run(VertxScheduleApplication.class, args);
    }

    @Override
    public void run(ApplicationArguments args) {
        String hostAddress = NetUtil.getLocalhostStr();
        Integer port = serverProperties.getPort();
        String contextPath = serverProperties.getServlet().getContextPath();
        String baseUrl = URLUtil.normalize("%s:%s%s".formatted(hostAddress, port, contextPath));
        logger.info("----------------------------------------------");
        logger.info("{} service started successfully.", "Vertx Schedule");
        logger.info("访问地址：{}", baseUrl);
        logger.info("在线文档：https://snailjob.opensnail.com");
        logger.info("----------------------------------------------");
    }

}
