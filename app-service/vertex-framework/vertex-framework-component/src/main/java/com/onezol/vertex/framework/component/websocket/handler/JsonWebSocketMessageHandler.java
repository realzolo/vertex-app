package com.onezol.vertex.framework.component.websocket.handler;

import com.onezol.vertex.framework.common.model.ObjectMethodMapping;
import com.onezol.vertex.framework.common.util.JsonUtils;
import com.onezol.vertex.framework.common.util.StringUtils;
import com.onezol.vertex.framework.component.websocket.message.DefaultWebSocketMessage;
import com.onezol.vertex.framework.component.websocket.message.JsonWebSocketMessage;
import com.onezol.vertex.framework.support.manager.async.AsyncTaskManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.lang.reflect.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Slf4j
public class JsonWebSocketMessageHandler extends TextWebSocketHandler {

    /**
     * topic 与 WebSocketMessageListener 的映射
     */
    private final Map<String, List<ObjectMethodMapping>> listeners = new HashMap<>();

    public JsonWebSocketMessageHandler(Map<String, List<ObjectMethodMapping>> listenersMap) {
        listeners.putAll(listenersMap);
    }

    @Override
    protected void handleTextMessage(@NonNull WebSocketSession session, TextMessage message) throws Exception {
        // 1 空消息，跳过
        if (message.getPayloadLength() == 0) {
            return;
        }
        // 2 ping 心跳消息，直接返回 pong 消息
        if (message.getPayloadLength() == 4 && Objects.equals(message.getPayload(), "ping")) {
            session.sendMessage(new TextMessage("pong"));
            return;
        }

        // 3 解析消息
        JsonWebSocketMessage jsonMessage;
        try {
            jsonMessage = JsonUtils.parseObject(message.getPayload(), JsonWebSocketMessage.class);
        } catch (Exception e) {
            log.error("[WebSocket][session({}) message({})] > 解析异常", session.getId(), message.getPayload());
            return;
        }
        if (StringUtils.isEmpty(jsonMessage.getGroup())) {
            log.error("[WebSocket][session({}) message({})] > 消息组为空", session.getId(), message.getPayload());
            return;
        }
        // 4 获得对应的 WebSocketMessageListener
        List<ObjectMethodMapping> messageListener = listeners.get(jsonMessage.getGroup());
        if (messageListener == null || messageListener.isEmpty()) {
            log.error("[WebSocket][session({}) message({})] > 监听器为空", session.getId(), message.getPayload());
            return;
        }
        // 5 遍历监听器, 调用对应的方法
        for (ObjectMethodMapping mapping : messageListener) {
            Object listener = mapping.getObject();
            Method method = mapping.getMethod();

            Type type = this.getArgumentType(method);
            if (type == Object.class) {
                return;
            }
            Object messagePayload;
            try {
                messagePayload = JsonUtils.parseObject(jsonMessage.getContent(), type);
            } catch (Exception e) {
                log.error("[WebSocket][session({}) message({})] > 解析消息内容异常", session.getId(), message.getPayload());
                return;
            }

            WebSocketMessage<Object> webSocketMessage = new DefaultWebSocketMessage<>(messagePayload);
            AsyncTaskManager.getInstance().execute(() -> {
                try {
                    method.invoke(listener, session, webSocketMessage);
                } catch (IllegalAccessException | InvocationTargetException e) {
                    throw new RuntimeException(e);
                }
            });
        }
    }

    private Type getArgumentType(Method method) {
        Parameter[] parameters = method.getParameters();
        Parameter genericParameter = parameters[1];
        Type genericType = genericParameter.getParameterizedType();
        if (genericType instanceof ParameterizedType parameterizedType) {
            Type[] actualTypeArguments = parameterizedType.getActualTypeArguments();
            return actualTypeArguments[0];
        }
        return Object.class;
    }
}
