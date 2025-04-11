package com.onezol.vertex.framework.component.message.mqtt.handler;

import com.onezol.vertex.framework.common.annotation.MessageListener;
import com.onezol.vertex.framework.common.model.ObjectMethodMapping;
import com.onezol.vertex.framework.component.message.mqtt.annotation.MQTTMessageListener;
import com.onezol.vertex.framework.component.message.mqtt.config.properties.MQTTProperties;
import com.onezol.vertex.framework.component.message.mqtt.helper.TopicHelper;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.mqtt.core.MqttPahoClientFactory;
import org.springframework.integration.mqtt.outbound.MqttPahoMessageHandler;
import org.springframework.integration.mqtt.support.DefaultPahoMessageConverter;
import org.springframework.integration.mqtt.support.MqttHeaders;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.MessagingException;
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
            String topic = headers.get(MqttHeaders.RECEIVED_TOPIC, String.class);
            this.dispatchMessage(topic, payload.toString(), headers);
        };
    }

    /**
     * 出站消息的消息处理器
     */
    @Bean
    @ServiceActivator(inputChannel = "mqttOutboundChannel")
    public MessageHandler mqttOutboundHandler() {
        MqttPahoMessageHandler handler = new MqttPahoMessageHandler(
                mqttProperties.getClientId() + "@publisher",
                mqttClientFactory
        );
        handler.setAsync(true);                                         // 异步发送
        handler.setDefaultTopic(mqttProperties.getOutboundDefault());   // 出站默认主题
        handler.setDefaultQos(1);                                       // 默认QoS
        handler.setDefaultRetained(false);                              // 默认不保留
        handler.setConverter(new DefaultPahoMessageConverter());        // 消息转换
        handler.setCompletionTimeout(5000);                             // 操作超时(ms)
        return handler;
    }


    /**
     * 错误消息的消息处理器
     */
    @Bean
    @ServiceActivator(inputChannel = "errorChannel")
    public MessageHandler errorHandler() {
        return message -> {
            MessageHeaders headers = message.getHeaders();
            String topic = headers.get(MqttHeaders.RECEIVED_TOPIC, String.class);
            MessagingException exception = (MessagingException) message.getPayload();
            log.error("[MQTT] [topic: {}] > 消息处理失败", topic, exception);
        };
    }

    /**
     * 分发消息: 根据消息主题分发消息给对应的消息处理器
     *
     * @param topic   消息主题
     * @param payload 消息内容
     * @param headers 消息头
     */
    private void dispatchMessage(String topic, String payload, MessageHeaders headers) {
        List<String> topics = this.listenerClasses.keySet()
                .stream()
                .filter(t -> TopicHelper.match(topic, t))
                .toList();
        if (topics.isEmpty()) {
            log.warn("[MQTT] [topic: {}] > 无法找到对应的消息处理器", topic);
            return;
        }

        for (String tpc : topics) {
            List<ObjectMethodMapping> objectMethodMappings = listenerClasses.get(tpc);
            for (ObjectMethodMapping objectMethodMapping : objectMethodMappings) {
                Object object = objectMethodMapping.getObject();
                Method method = objectMethodMapping.getMethod();
                try {
                    method.invoke(object, headers, payload);
                } catch (Exception e) {
                    log.error("[MQTT] [topic: {}] > 消息处理器执行异常", topic, e);
                }
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
