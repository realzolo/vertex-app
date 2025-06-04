package com.onezol.vertx;

import com.onezol.vertx.framework.common.constant.GenericConstants;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.core.env.Environment;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.TimeZone;

@SpringBootApplication
public class VertxApplication {

    public static void main(String[] args) throws UnknownHostException {
        TimeZone.setDefault(TimeZone.getTimeZone(GenericConstants.DEFAULT_TIME_ZONE));

        ApplicationContext context = SpringApplication.run(VertxApplication.class, args);
        Environment env = context.getEnvironment();
        String ip = InetAddress.getLocalHost().getHostAddress();
        String port = env.getProperty("server.port", "8080");

        System.out.println(
                """
                         -------------------------------- Vertx App --------------------------------
                         Vertx App is running!
                         API接口文档: http://{ip}:{port}/doc.html
                         Actuator健康检查: http://{ip}:{port}/actuator/health
                         ---------------------------------------------------------------------------
                        """.replaceAll("\\{ip}", ip).replaceAll("\\{port}", port)
        );
    }

}
