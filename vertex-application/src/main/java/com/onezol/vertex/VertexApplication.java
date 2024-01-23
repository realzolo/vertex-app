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

        logger.info(String.format("""
                Vertex Application is running! Swagger文档: http://%s:%s%s/doc.html
                """, ip, port, path));
    }

}
