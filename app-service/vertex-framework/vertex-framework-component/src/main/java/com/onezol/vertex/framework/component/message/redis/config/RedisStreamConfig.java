package com.onezol.vertex.framework.component.message.redis.config;

import com.onezol.vertex.framework.common.annotation.MessageListener;
import com.onezol.vertex.framework.common.model.ObjectMethodMapping;
import com.onezol.vertex.framework.component.message.redis.annotation.RedisMessageListener;
import com.onezol.vertex.framework.component.message.redis.listener.DispatchMessageListener;
import jakarta.annotation.PreDestroy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisServerCommands;
import org.springframework.data.redis.connection.stream.*;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.stream.StreamMessageListenerContainer;
import org.springframework.data.redis.stream.Subscription;
import org.springframework.util.Assert;

import java.lang.reflect.Method;
import java.time.Duration;
import java.util.*;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
@Configuration
public class RedisStreamConfig implements InitializingBean {
    public final StringRedisTemplate redisTemplate;
    private final List<StreamMessageListenerContainer<String, MapRecord<String, String, String>>> containers = new ArrayList<>();

    public RedisStreamConfig(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        checkRedisVersion();
    }

    @PreDestroy
    public void destroy() {
        containers.forEach(StreamMessageListenerContainer::stop);
    }

    @Bean
    public List<Subscription> subscription(ApplicationContext context, RedisConnectionFactory connectionFactory, DispatchMessageListener dispatchMessageListener) {
        // 获取消息监听器
        Map<String, List<ObjectMethodMapping>> streamListeners = getListeners(context);
        dispatchMessageListener.setListeners(streamListeners);
        Map<String, List<String>> streamGroups = this.getStreamGroup(streamListeners);

        AtomicInteger index = new AtomicInteger(1);
        int processors = Runtime.getRuntime().availableProcessors();
        ThreadPoolExecutor executor = new ThreadPoolExecutor(processors, processors, 0, TimeUnit.SECONDS, new LinkedBlockingDeque<>(), r -> {
            Thread thread = new Thread(r);
            thread.setName("async-stream-consumer-" + index.getAndIncrement());
            thread.setDaemon(true);
            return thread;
        });
        // 定义参数并创建 StreamMessageListenerContainer 对象
        StreamMessageListenerContainer.StreamMessageListenerContainerOptions<String, MapRecord<String, String, String>> options =
                StreamMessageListenerContainer
                        .StreamMessageListenerContainerOptions
                        .builder()
                        .batchSize(5)
                        .executor(executor)
                        .pollTimeout(Duration.ofSeconds(1))
                        .build();

        // 创建消息侦听器容器
        List<Subscription> subscriptions = new ArrayList<>();
        for (Map.Entry<String, List<String>> entry : streamGroups.entrySet()) {
            String stream = entry.getKey();
            List<String> groups = entry.getValue();
            this.initStream(stream, groups);
            StreamMessageListenerContainer<String, MapRecord<String, String, String>> container = StreamMessageListenerContainer.create(connectionFactory, options);
            for (String group : groups) {
                Consumer consumer = Consumer.from(group, "consumer-" + group);
                Subscription subscription = container.receive(consumer, StreamOffset.create(stream, ReadOffset.lastConsumed()), dispatchMessageListener);
                subscriptions.add(subscription);
            }
            container.start();
            containers.add(container);
        }
        return subscriptions;
    }

    private void initStream(String stream, List<String> groups) {
        boolean hasKey = Boolean.TRUE.equals(redisTemplate.hasKey(stream));
        if (hasKey) {
            return;
        }
        Map<String, Object> map = new HashMap<>(1);
        map.put("field", "value");
        //创建主题
        RecordId recordId = redisTemplate.opsForStream().add(stream, map);
        //创建消费组
        for (String group : groups) {
            redisTemplate.opsForStream().createGroup(stream, group);
        }
        //将初始化的值删除掉
        redisTemplate.opsForStream().delete(stream, recordId);
        log.info("stream:{}-group:{} initialize success", stream, groups);
    }

    /**
     * 检查 Redis 版本是否符合要求
     *
     * @throws IllegalStateException 如果 Redis 版本小于 5.0.0 版本，抛出该异常
     */
    private void checkRedisVersion() {
        // 获得 Redis 版本  
        Properties info = (Properties) redisTemplate.execute((RedisCallback<Properties>) RedisServerCommands::info);
        Assert.notNull(info, "Redis info is null");
        Object redisVersion = info.get("redis_version");
        String[] vars = redisVersion.toString().split("\\.");
        int anInt = Integer.parseInt(vars[0]);
        if (anInt < 5) {
            throw new IllegalStateException(String.format("您当前的 Redis 版本为 %s，小于最低要求的 5.0.0 版本！", redisVersion));
        }
    }

    private Map<String, List<ObjectMethodMapping>> getListeners(ApplicationContext context) {
        Map<String, Object> messageListeners = context.getBeansWithAnnotation(MessageListener.class);
        Collection<Object> listeners = messageListeners.values();
        Map<String, List<ObjectMethodMapping>> listenerClasses = new HashMap<>();
        for (Object listener : listeners) {
            Method[] methods = listener.getClass().getMethods();
            for (Method method : methods) {
                if (method.isAnnotationPresent(RedisMessageListener.class)) {
                    RedisMessageListener annotation = method.getAnnotation(RedisMessageListener.class);
                    String stream = annotation.stream();
                    String group = annotation.group();
                    String key = String.format("%s:%s", stream, group);
                    ObjectMethodMapping methodGroupMapping = new ObjectMethodMapping(listener, method);
                    List<ObjectMethodMapping> mappings = listenerClasses.getOrDefault(key, new ArrayList<>());
                    mappings.add(methodGroupMapping);
                    listenerClasses.put(key, mappings);
                }
            }
        }
        return listenerClasses;
    }

    private Map<String, List<String>> getStreamGroup(Map<String, List<ObjectMethodMapping>> listeners) {
        Set<String> streamGroupKV = listeners.keySet();
        Map<String, List<String>> streamGroup = new HashMap<>();
        for (String kv : streamGroupKV) {
            String[] split = kv.split(":");
            String stream = split[0];
            String group = split[1];
            List<String> groups = streamGroup.getOrDefault(stream, new ArrayList<>());
            groups.add(group);
            streamGroup.put(stream, groups);
        }
        return streamGroup;
    }
}
