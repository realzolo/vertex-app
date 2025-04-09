package com.onezol.vertex.framework.component.message.redis.config;

import com.onezol.vertex.framework.common.annotation.MessageListener;
import com.onezol.vertex.framework.common.model.ObjectMethodMapping;
import com.onezol.vertex.framework.component.message.redis.annotation.RedisMessageListener;
import com.onezol.vertex.framework.support.support.RedisStream;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisServerCommands;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.util.Assert;

import java.lang.reflect.Method;
import java.util.*;

@Slf4j
@Configuration
public class RedisStreamConfig {

    public final ApplicationContext applicationContext;

    public final StringRedisTemplate redisTemplate;

    public final RedisStream redisStream;

    public RedisStreamConfig(ApplicationContext applicationContext, StringRedisTemplate redisTemplate, RedisStream redisStream) {
        this.applicationContext = applicationContext;
        this.redisTemplate = redisTemplate;
        this.redisStream = redisStream;
    }

    @PostConstruct
    public void postConstruct() {
        checkRedisVersion();
        initSubscript();
    }

    public void initSubscript() {
        Map<String, List<ObjectMethodMapping>> listeners = this.getListeners();
        Map<String, List<String>> streamGroup = this.getStreamGroup(listeners);
        for (Map.Entry<String, List<String>> entry : streamGroup.entrySet()) {
            String stream = entry.getKey();
            List<String> groups = entry.getValue();
            for (String group : groups) {
                List<ObjectMethodMapping> mappings = listeners.get(stream + ":" + group);
                redisStream.subscribe(stream, group, mappings);
            }
        }
    }


    /**
     * 检查 Redis 版本是否符合要求
     *
     * @throws IllegalStateException 如果 Redis 版本小于 5.0.0 版本，抛出该异常
     */
    private void checkRedisVersion() {
        // 获得 Redis 版本  
        Properties info = redisTemplate.execute((RedisCallback<Properties>) RedisServerCommands::info);
        Assert.notNull(info, "Redis info is null");
        Object redisVersion = info.get("redis_version");
        String[] vars = redisVersion.toString().split("\\.");
        int anInt = Integer.parseInt(vars[0]);
        if (anInt < 5) {
            throw new IllegalStateException(String.format("您当前的 Redis 版本为 %s，小于最低要求的 5.0.0 版本！", redisVersion));
        }
    }

    private Map<String, List<ObjectMethodMapping>> getListeners() {
        Map<String, Object> messageListeners = applicationContext.getBeansWithAnnotation(MessageListener.class);
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
            if (!groups.contains(group)) {
                groups.add(group);
            }
            streamGroup.put(stream, groups);
        }
        return streamGroup;
    }
}
