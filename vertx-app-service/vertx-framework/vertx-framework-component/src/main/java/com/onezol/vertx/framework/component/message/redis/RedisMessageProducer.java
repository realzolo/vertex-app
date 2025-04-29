package com.onezol.vertx.framework.component.message.redis;

import com.onezol.vertx.framework.common.util.ThreadUtils;
import com.onezol.vertx.framework.support.support.RedisStream;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class RedisMessageProducer {

    private final RedisStream redisStream;

    public RedisMessageProducer(RedisStream redisStream) {
        this.redisStream = redisStream;
    }

    @PostConstruct
    public void init() {
//        AsyncTaskManager.getInstance().execute(this::doSomething);
    }

    public void doSomething() {
        for (int i = 0; i < 100; i++) {
            redisStream.publish("vertx-application", "key", "value: " + i);
            ThreadUtils.sleep(500);
        }
    }
}
