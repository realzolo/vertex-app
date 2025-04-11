package com.onezol.vertex.framework.component.message.mqtt.config;

import com.onezol.vertex.framework.component.message.mqtt.config.properties.MQTTProperties;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.core.MessageProducer;
import org.springframework.integration.mqtt.core.DefaultMqttPahoClientFactory;
import org.springframework.integration.mqtt.core.MqttPahoClientFactory;
import org.springframework.integration.mqtt.inbound.MqttPahoMessageDrivenChannelAdapter;
import org.springframework.integration.mqtt.support.DefaultPahoMessageConverter;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;

@Slf4j
@Configuration
@ConditionalOnBean(MQTTProperties.class)
public class MQTTConfiguration {

    private final MQTTProperties mqttProperties;

    public MQTTConfiguration(MQTTProperties mqttProperties) {
        this.mqttProperties = mqttProperties;
    }

    @Bean
    public MqttPahoClientFactory mqttClientFactory() {
        DefaultMqttPahoClientFactory factory = new DefaultMqttPahoClientFactory();
        MqttConnectOptions options = new MqttConnectOptions();

        options.setServerURIs(new String[]{mqttProperties.getBrokerUrl()});
        options.setUserName(mqttProperties.getUsername());
        options.setPassword(mqttProperties.getPassword().toCharArray());
        options.setConnectionTimeout(30);       // 连接超时时间(秒)
        options.setKeepAliveInterval(120);      // KeepAlive间隔(秒)
        options.setCleanSession(false);         // 保留会话状态
        options.setAutomaticReconnect(true);    // 启用自动重连
        options.setMaxInflight(100);            // 设置最大未完成发布消息数

        factory.setConnectionOptions(options);
        return factory;
    }

    /**
     * 入站通道（用于接收消息）
     */
    @Bean
    public MessageChannel mqttInboundChannel() {
        return new DirectChannel();
    }

    /**
     * 出站通道（用于发送消息）
     */
    @Bean
    public MessageChannel mqttOutboundChannel() {
        return new DirectChannel();
    }

    /**
     * 入站消息的消息生产者
     */
    @Bean
    public MessageProducer inbound() {
        MqttPahoMessageDrivenChannelAdapter adapter = new MqttPahoMessageDrivenChannelAdapter(
                mqttProperties.getClientId() + "@subscriber",
                mqttClientFactory(),
                mqttProperties.getInboundDefault()
        );

        adapter.setCompletionTimeout(5000);
        adapter.setQos(1);
        adapter.setOutputChannel(mqttInboundChannel());

        // 添加消息转换器
        adapter.setConverter(new DefaultPahoMessageConverter() {
            @Override
            public Message<?> toMessage(String topic, MqttMessage mqttMessage) {
                // 丢弃大消息
                if (mqttMessage.getPayload().length > 1024) {
                    log.warn("[MQTT] [topic: {}] > 消息长度超过限制，自动丢弃", topic);
                    return null;
                }
                return super.toMessage(topic, mqttMessage);
            }
        });
        return adapter;
    }

}
