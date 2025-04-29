package com.onezol.vertx.websocket;

import com.onezol.vertx.framework.common.annotation.MessageListener;
import com.onezol.vertx.framework.component.websocket.annotation.WebSocketMessageListener;
import com.onezol.vertx.websocket.model.RedisMessagePayload;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.stream.RecordId;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;

import java.util.HashMap;
import java.util.Map;


@MessageListener
public class DemoWebSocketMessageListener {
    @Autowired
    private RedisTemplate redisTemplate;

    @WebSocketMessageListener(group = "redis")
    public void onMessage(WebSocketSession session, WebSocketMessage<RedisMessagePayload> message) {
        RedisMessagePayload payload = message.getPayload();
        System.out.println("[redis]WebSocket消息：" + payload);
        Map<String, String> msg = new HashMap<>();
        msg.put("toGroup", payload.getGroup());
        msg.put("content", "我是一条来自WebSocket发出的消息哦~");
        RecordId recordId = redisTemplate.opsForStream().add(payload.getStream(), msg);
        if (recordId != null) {
            System.out.println("[redis]消息发送成功，消息ID：" + recordId);
        } else {
            System.out.println("[redis]消息发送失败");
        }
    }
}
