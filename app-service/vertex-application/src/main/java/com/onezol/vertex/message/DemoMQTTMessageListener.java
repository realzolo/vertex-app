package com.onezol.vertex.message;

import com.onezol.vertex.framework.common.annotation.MessageListener;
import com.onezol.vertex.framework.component.message.mqtt.annotation.MQTTMessageListener;

@MessageListener
public class DemoMQTTMessageListener {

    @MQTTMessageListener(topic = "/demo/mqtt")
    public void onMessage1(String message) {
        System.out.println("Received MQTT1 message: " + message);
    }

    @MQTTMessageListener(topic = "/demo/mqtt")
    public void onMessage2(String message) {
        System.out.println("Received MQTT2 message: " + message);
    }

    @MQTTMessageListener(topic = "/demo/mqtt3")
    public void onMessage3(String message) {
        System.out.println("Received MQTT3 message: " + message);
    }

    @MQTTMessageListener(topic = "/demo/#")
    public void onMessage4(String message) {
        System.out.println("Received MQTT4 message: " + message);
    }

}
