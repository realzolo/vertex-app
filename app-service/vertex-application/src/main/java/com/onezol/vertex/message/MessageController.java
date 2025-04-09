package com.onezol.vertex.message;

import com.onezol.vertex.framework.component.message.mqtt.MQTTGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/message")
public class MessageController {

    @Autowired
    private MQTTGateway mqttGateway;

    @RequestMapping("/mqtt/send/{topic}/{message}")
    public void send(@PathVariable("topic") String topic, @PathVariable("message") String message) {
        mqttGateway.send(topic, message);
    }


}
