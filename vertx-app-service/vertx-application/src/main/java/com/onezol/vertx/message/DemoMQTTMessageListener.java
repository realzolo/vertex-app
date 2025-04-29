package com.onezol.vertx.message;

import com.onezol.vertx.framework.common.annotation.MessageListener;
import com.onezol.vertx.framework.component.message.mqtt.MQTTGateway;
import com.onezol.vertx.framework.component.message.mqtt.annotation.MQTTMessageListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.MessageHeaders;

@MessageListener
public class DemoMQTTMessageListener {

    private final MQTTGateway mqttGateway;

    public DemoMQTTMessageListener(@Autowired(required = false) MQTTGateway mqttGateway) {
        this.mqttGateway = mqttGateway;
    }

    @MQTTMessageListener(topic = "/demo/mqtt")
    public void onMessage1(MessageHeaders headers, String message) {
        System.out.println("Received MQTT1 message: " + message);
    }

    @MQTTMessageListener(topic = "/demo/mqtt")
    public void onMessage2(MessageHeaders headers, String message) {
        System.out.println("Received MQTT2 message: " + message);
    }

    @MQTTMessageListener(topic = "/demo/mqtt3")
    public void onMessage3(MessageHeaders headers, String message) {
        System.out.println("Received MQTT3 message: " + message);
    }

    @MQTTMessageListener(topic = "/demo/#")
    public void onMessage4(MessageHeaders headers, String message) {
        System.out.println("Received MQTT4 message: " + message);
    }

    @MQTTMessageListener(topic = "/server/#")
    public void onMessage5(MessageHeaders headers, String message) {
        System.out.println("Received MQTT5 message: " + message);
    }

    @MQTTMessageListener(topic = "/sys/+/+/thing/event")
    public void onMessage6(MessageHeaders headers, String message) {
        System.out.println("Received MQTT6 message: " + message);
    }

    @MQTTMessageListener(topic = "/sys/+/+/thing/+")
    public void onMessage7(MessageHeaders headers, String message) {
        System.out.println("Received MQTT7 message: " + message);
    }

    @MQTTMessageListener(topic = "/sys/+/+/thing/#")
    public void onMessage8(MessageHeaders headers, String message) {
        System.out.println("Received MQTT8 message: " + message);
    }

    @MQTTMessageListener(topic = "/rotary")
    public void rotary(MessageHeaders headers, String message) {
        String[] vars = message.split(":");
        for (int i = 0; i < 100; i++) {
            mqttGateway.send("/rotary/receive/" + vars[0] + i, 2, true, vars[1] + ":" + i);
        }
    }

    @MQTTMessageListener(topic = "/rotary/receive/#")
    public void rotaryReceive(MessageHeaders headers, String message) {
        String topic = headers.get("mqtt_receivedTopic", String.class);
        System.out.println("Received MQTT message: " + message + " from topic: " + topic);
    }

}
