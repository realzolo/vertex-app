package com.onezol.vertex.message;

import com.onezol.vertex.framework.common.annotation.MessageListener;
import com.onezol.vertex.framework.component.message.redis.annotation.RedisMessageListener;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.stream.MapRecord;
import org.springframework.data.redis.connection.stream.PendingMessagesSummary;
import org.springframework.data.redis.core.RedisTemplate;

@Slf4j
@MessageListener
public class DemoRedisMessageListener {
    @Autowired
    RedisTemplate redisTemplate;

    @RedisMessageListener(stream = "stream1", group = "group1")
    public void onMessage(MapRecord<String, String, String> record) {
        System.out.println("[stream1-group1]接收Redis到消息：" + record);
        PendingMessagesSummary pendingSummary = redisTemplate.opsForStream().pending("stream1", "group1");
        System.out.println(pendingSummary.getTotalPendingMessages());

        Long acknowledge = redisTemplate.opsForStream().acknowledge("stream1", "group1", record.getId());
        System.out.println("[stream1-group1]确认消息：" + acknowledge);
        PendingMessagesSummary pendingSummary1 = redisTemplate.opsForStream().pending("stream1", "group1");
        System.out.println(pendingSummary1.getTotalPendingMessages());
    }

    @RedisMessageListener(stream = "stream1", group = "group2")
    public void onMessage1(MapRecord<String, String, String> record) {
        System.out.println("[stream1-group2]接收Redis到消息：" + record);
        PendingMessagesSummary pendingSummary = redisTemplate.opsForStream().pending("stream1", "group1");
        System.out.println("pendingSummary.getTotalPendingMessages()2222: " + pendingSummary.getTotalPendingMessages());
    }

    @RedisMessageListener(stream = "demo-stream2", group = "demo-group")
    public void onMessage2(MapRecord<String, String, String> record) {
        System.out.println("接收Redis到消息：" + record);
    }
}
