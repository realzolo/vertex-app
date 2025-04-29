package com.onezol.vertx.framework.component.websocket.config;

import com.onezol.vertx.framework.common.annotation.MessageListener;
import com.onezol.vertx.framework.common.model.ObjectMethodMapping;
import com.onezol.vertx.framework.component.websocket.annotation.WebSocketMessageListener;
import com.onezol.vertx.framework.component.websocket.handler.JsonWebSocketMessageHandler;
import com.onezol.vertx.framework.component.websocket.session.WebSocketSessionHandlerDecorator;
import com.onezol.vertx.framework.component.websocket.session.WebSocketSessionManager;
import com.onezol.vertx.framework.component.websocket.session.WebSocketSessionManagerImpl;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;

import java.lang.reflect.Method;
import java.util.*;

@Configuration
@EnableWebSocket
public class WebSocketConfiguration {

    @Bean
    public WebSocketConfigurer webSocketConfigurer(WebSocketHandler webSocketHandler) {
        return registry -> registry
                // 添加 WebSocketHandler
                .addHandler(webSocketHandler, "/ws")
                // 允许跨域，否则前端连接会直接断开
                .setAllowedOriginPatterns("*");
    }

    @Bean
    public WebSocketHandler webSocketHandler(ApplicationContext context, WebSocketSessionManager sessionManager) {
        Map<String, List<ObjectMethodMapping>> listeners = this.getListeners(context);
        // 1. 创建 JsonWebSocketMessageHandler 对象，处理消息
        JsonWebSocketMessageHandler messageHandler = new JsonWebSocketMessageHandler(listeners);
        // 2. 创建 WebSocketSessionHandlerDecorator 对象，处理连接
        return new WebSocketSessionHandlerDecorator(messageHandler, sessionManager);
    }

    @Bean
    public WebSocketSessionManager webSocketSessionManager() {
        return new WebSocketSessionManagerImpl();
    }

    private Map<String, List<ObjectMethodMapping>> getListeners(ApplicationContext context) {
        Map<String, Object> messageListeners = context.getBeansWithAnnotation(MessageListener.class);
        Collection<Object> listeners = messageListeners.values();
        Map<String, List<ObjectMethodMapping>> listenerClasses = new HashMap<>();
        for (Object listener : listeners) {
            Method[] methods = listener.getClass().getMethods();
            for (Method method : methods) {
                if (method.isAnnotationPresent(WebSocketMessageListener.class)) {
                    WebSocketMessageListener annotation = method.getAnnotation(WebSocketMessageListener.class);
                    String group = annotation.group();
                    ObjectMethodMapping methodGroupMapping = new ObjectMethodMapping(listener, method);
                    List<ObjectMethodMapping> mappings = listenerClasses.getOrDefault(group, new ArrayList<>());
                    mappings.add(methodGroupMapping);
                    listenerClasses.put(group, mappings);
                }
            }
        }
        return listenerClasses;
    }
}
