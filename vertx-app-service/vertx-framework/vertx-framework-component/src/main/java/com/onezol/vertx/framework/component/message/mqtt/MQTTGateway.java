package com.onezol.vertx.framework.component.message.mqtt;

import com.onezol.vertx.framework.component.message.mqtt.config.properties.MQTTProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.integration.annotation.MessagingGateway;
import org.springframework.integration.mqtt.support.MqttHeaders;
import org.springframework.messaging.handler.annotation.Header;

@ConditionalOnBean(MQTTProperties.class)
@MessagingGateway(defaultRequestChannel = "mqttOutboundChannel")
public interface MQTTGateway {

    /**
     * 发送MQTT消息
     *
     * @param topic   主题
     * @param payload 内容
     */
    void send(@Header(MqttHeaders.TOPIC) String topic, String payload);

    /**
     * 发送包含qos的消息
     *
     * @param topic   主题
     * @param qos     对消息处理的几种机制。<br>
     *                0 表示的是订阅者没收到消息不会再次发送，消息会丢失。<br>
     *                1 表示的是会尝试重试，一直到接收到消息，但这种情况可能导致订阅者收到多次重复消息。<br>
     *                2 多了一次去重的动作，确保订阅者收到的消息有一次。
     * @param payload 消息体
     */
    void send(@Header(MqttHeaders.TOPIC) String topic, @Header(MqttHeaders.QOS) int qos, String payload);

    /**
     * 发送MQTT消息
     *
     * @param topic    主题
     * @param retained 是否保留消息 (默认为false，表示不保留，即如果消息发送成功，则服务器会删除这个消息)
     */
    void send(@Header(MqttHeaders.TOPIC) String topic, @Header(MqttHeaders.RETAINED) boolean retained, String payload);

    /**
     * 发送MQTT消息
     *
     * @param topic    主题
     * @param qos      对消息处理的几种机制。<br>
     *                 0 表示的是订阅者没收到消息不会再次发送，消息会丢失。<br>
     *                 1 表示的是会尝试重试，一直到接收到消息，但这种情况可能导致订阅者收到多次重复消息。<br>
     *                 2 多了一次去重的动作，确保订阅者收到的消息有一次。
     * @param retained 是否保留消息 (默认为false，表示不保留，即如果消息发送成功，则服务器会删除这个消息)
     * @param payload  内容
     */
    void send(@Header(MqttHeaders.TOPIC) String topic, @Header(MqttHeaders.QOS) int qos, @Header(MqttHeaders.RETAINED) boolean retained, String payload);

}
