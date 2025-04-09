package com.onezol.vertex.framework.support.config;

import com.onezol.vertex.framework.support.config.properties.MQTTProperties;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.core.MessageProducer;
import org.springframework.integration.mqtt.core.DefaultMqttPahoClientFactory;
import org.springframework.integration.mqtt.core.MqttPahoClientFactory;
import org.springframework.integration.mqtt.inbound.MqttPahoMessageDrivenChannelAdapter;
import org.springframework.integration.mqtt.support.DefaultPahoMessageConverter;
import org.springframework.messaging.MessageChannel;

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
        options.setCleanSession(true);
        options.setUserName(mqttProperties.getUsername());
        options.setPassword(mqttProperties.getPassword().toCharArray());
        options.setConnectionTimeout(30);
        options.setKeepAliveInterval(60);
        options.setAutomaticReconnect(true);

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
    public MessageProducer messageProducer() {
        MqttPahoMessageDrivenChannelAdapter adapter = new MqttPahoMessageDrivenChannelAdapter(
                mqttProperties.getClientId() + "-subscriber",
                mqttClientFactory(),
                mqttProperties.getTopic()
        );

        adapter.setCompletionTimeout(5000);
        adapter.setConverter(new DefaultPahoMessageConverter());
        adapter.setQos(1);
        adapter.setOutputChannel(mqttInboundChannel());
        return adapter;
    }

}
