package com.onezol.vertex.framework.support.support;

import com.onezol.vertex.framework.common.model.ObjectMethodMapping;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RStream;
import org.redisson.api.RedissonClient;
import org.redisson.api.StreamGroup;
import org.redisson.api.StreamMessageId;
import org.redisson.api.stream.StreamAddArgs;
import org.redisson.api.stream.StreamCreateGroupArgs;
import org.redisson.api.stream.StreamReadGroupArgs;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class RedisStream {

    private final RedissonClient redissonClient;

    public RedisStream(RedissonClient redissonClient) {
        this.redissonClient = redissonClient;
    }

    public void publish(String streamName, String key, String message) {
        RStream<Object, Object> stream = redissonClient.getStream(streamName);
        stream.add(StreamAddArgs.entry(key, message));
    }

    @Async
    public void subscribe(String streamName, String groupName, List<ObjectMethodMapping> mappings) {
        RStream<Object, Object> stream = redissonClient.getStream(streamName);
        List<StreamGroup> streamGroups = stream.listGroups();
        if (streamGroups.stream().noneMatch(group -> group.getName().equals(groupName))) {
            stream.createGroup(StreamCreateGroupArgs.name(groupName));
        }

        while (!Thread.currentThread().isInterrupted()) {
            StreamReadGroupArgs streamReadGroupArgs = StreamReadGroupArgs.neverDelivered().count(1);
            Map<StreamMessageId, Map<Object, Object>> messages = stream.readGroup(groupName, groupName, streamReadGroupArgs);
            for (Map.Entry<StreamMessageId, Map<Object, Object>> entry : messages.entrySet()) {
                StreamMessageId streamMessageId = entry.getKey();
                Map<Object, Object> message = entry.getValue();

                ObjectMethodMapping mapping = mappings.get((int) (Math.random() * mappings.size()));
                try {
                    mapping.getMethod().invoke(mapping.getObject(), message);
                } catch (Exception e) {
                    log.error("RedisStreamMessageQueue method invoke error: {}", e.getMessage());
                }

                stream.ack(groupName, streamMessageId);
            }
        }
    }

}
