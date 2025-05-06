package com.onezol.vertx;

import com.onezol.vertx.framework.common.constant.GenericConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.core.env.Environment;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.TimeZone;

@SuppressWarnings("ALL")
@SpringBootApplication
public class VertxApplication {

    private static final Logger logger = LoggerFactory.getLogger(VertxApplication.class);

    public static void main(String[] args) throws UnknownHostException {
        TimeZone.setDefault(TimeZone.getTimeZone(GenericConstants.DEFAULT_TIME_ZONE));

        ApplicationContext context = SpringApplication.run(VertxApplication.class, args);
        Environment env = context.getEnvironment();
        String ip = InetAddress.getLocalHost().getHostAddress();
        String port = env.getProperty("server.port", "8080");
        String path = env.getProperty("server.servlet.context-path", "");

        logger.info("""
                Vertx App is running! 控制台首页: http://{ip}:{port} Swagger文档: http://{ip}:{port}/doc.html 
                """.replaceAll("\\{ip}", ip).replaceAll("\\{port}", port));
    }

}
