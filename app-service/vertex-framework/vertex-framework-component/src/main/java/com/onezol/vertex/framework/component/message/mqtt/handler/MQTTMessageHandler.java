package com.onezol.vertex.framework.component.message.mqtt.handler;

import com.onezol.vertex.framework.common.annotation.MessageListener;
import com.onezol.vertex.framework.common.model.ObjectMethodMapping;
import com.onezol.vertex.framework.component.message.mqtt.annotation.MQTTMessageListener;
import com.onezol.vertex.framework.support.config.properties.MQTTProperties;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.mqtt.core.MqttPahoClientFactory;
import org.springframework.integration.mqtt.outbound.MqttPahoMessageHandler;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.MessageHeaders;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.*;

@Slf4j
@Component
@ConditionalOnBean(MQTTProperties.class)
public class MQTTMessageHandler {

    private final ApplicationContext applicationContext;

    private final MQTTProperties mqttProperties;

    private final MqttPahoClientFactory mqttClientFactory;

    private Map<String, List<ObjectMethodMapping>> listenerClasses;

    public MQTTMessageHandler(ApplicationContext applicationContext, MQTTProperties mqttProperties, MqttPahoClientFactory mqttClientFactory) {
        this.applicationContext = applicationContext;
        this.mqttProperties = mqttProperties;
        this.mqttClientFactory = mqttClientFactory;
    }

    @PostConstruct
    public void init() {
        this.listenerClasses = this.getListeners();
    }

    /**
     * 入站消息的消息处理器
     */
    @Bean
    @ServiceActivator(inputChannel = "mqttInboundChannel")
    public MessageHandler mqttInboundHandler() {
        return message -> {
            MessageHeaders headers = message.getHeaders();
            Object payload = message.getPayload();
            String topic = String.valueOf(headers.get("mqtt_receivedTopic"));
            this.dispatchMessage(topic, payload.toString());
        };
    }

    /**
     * 出站消息的消息处理器
     */
    @Bean
    @ServiceActivator(inputChannel = "mqttOutboundChannel")
    public MessageHandler mqttOutboundHandler() {
        MqttPahoMessageHandler messageHandler = new MqttPahoMessageHandler(mqttProperties.getClientId() + "-publisher", mqttClientFactory);
        messageHandler.setAsync(true);
        messageHandler.setDefaultTopic(mqttProperties.getTopic());
        return messageHandler;
    }

    private void dispatchMessage(String topic, String payload) {
        List<ObjectMethodMapping> objectMethodMappings = this.listenerClasses.get(topic);
        if (Objects.isNull(objectMethodMappings)) {
            return;
        }

        for (ObjectMethodMapping objectMethodMapping : objectMethodMappings) {
            Object object = objectMethodMapping.getObject();
            Method method = objectMethodMapping.getMethod();
            try {
                method.invoke(object, payload);
            } catch (Exception e) {
                log.error("[MQTT][topic({}) payload({})] > 调用消息处理器异常", topic, payload, e);
            }
        }
    }

    private Map<String, List<ObjectMethodMapping>> getListeners() {
        Map<String, Object> messageListeners = applicationContext.getBeansWithAnnotation(MessageListener.class);
        Collection<Object> listeners = messageListeners.values();
        Map<String, List<ObjectMethodMapping>> listenerClasses = new HashMap<>();
        for (Object listener : listeners) {
            Method[] methods = listener.getClass().getMethods();
            for (Method method : methods) {
                if (method.isAnnotationPresent(MQTTMessageListener.class)) {
                    MQTTMessageListener annotation = method.getAnnotation(MQTTMessageListener.class);
                    String topic = annotation.topic();
                    ObjectMethodMapping methodGroupMapping = new ObjectMethodMapping(listener, method);
                    List<ObjectMethodMapping> mappings = listenerClasses.getOrDefault(topic, new ArrayList<>());
                    mappings.add(methodGroupMapping);
                    listenerClasses.put(topic, mappings);
                }
            }
        }
        return listenerClasses;
    }

}
