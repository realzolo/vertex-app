package com.onezol.vertex.message;

import com.onezol.vertex.framework.common.annotation.MessageListener;
import com.onezol.vertex.framework.component.message.redis.annotation.RedisMessageListener;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

@Slf4j
@MessageListener
public class DemoRedisMessageListener {

    @RedisMessageListener(stream = "vertex-application", group = "demo-group")
    public void onMessage1(Map<Object, Object> message) {
        System.out.println("Client 1 -> 接收Redis到消息：" + message);
    }

    @RedisMessageListener(stream = "vertex-application", group = "demo-group")
    public void onMessage2(Map<Object, Object> message) {
        System.out.println("Client 2 -> 接收Redis到消息：" + message);
    }

}
