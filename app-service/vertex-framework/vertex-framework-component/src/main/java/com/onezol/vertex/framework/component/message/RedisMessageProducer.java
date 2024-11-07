package com.onezol.vertex.framework.component.message;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.stream.RecordId;
import org.springframework.data.redis.connection.stream.StreamRecords;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Collections;

@Slf4j
@Component
public class RedisMessageProducer {

    private final RedisTemplate redisTemplate;

    public RedisMessageProducer(RedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public void sendMessage(Object message) {
//        RecordId recordId = redisTemplate
//                .opsForStream().add(StreamRecords.newRecord()
//                        .ofMap(Collections.singletonMap("data", message))
//                        .withStreamKey(streamKey));
//        if (recordId != null) {
//            log.info("Message sent to Stream '{}' with RecordId: {}", streamKey, recordId);
//        }
    }
}
