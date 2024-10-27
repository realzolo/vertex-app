package com.onezol.vertex;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.Environment;

import java.net.InetAddress;
import java.net.UnknownHostException;

@SuppressWarnings("ALL")
@SpringBootApplication
public class VertexApplication {
    private static final Logger logger = LoggerFactory.getLogger(VertexApplication.class);

    public static void main(String[] args) throws UnknownHostException {
        ConfigurableApplicationContext context = SpringApplication.run(VertexApplication.class, args);
        Environment env = context.getEnvironment();
        String ip = InetAddress.getLocalHost().getHostAddress();
        String port = env.getProperty("server.port", "8080");
        String path = env.getProperty("server.servlet.context-path", "");

        logger.info("""
                Vertex Application is running! 控制台首页: http://{ip}:{port} Swagger文档: http://{ip}:{port}/doc.html 
                """.replaceAll("\\{ip}", ip).replaceAll("\\{port}", port));
    }

}
