package com.onezol.vertex.framework.component.message.mqtt.config.properties;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "spring.mqtt")
@ConditionalOnProperty(prefix = "spring.mqtt", name = "enabled", havingValue = "true")
public class MQTTProperties {

    @Schema(name = "MQTT Broker URL")
    private String brokerUrl;

    @Schema(name = "MQTT Client ID")
    private String clientId;

    @Schema(name = "MQTT Username")
    private String username;

    @Schema(name = "MQTT Password")
    private String password;

    @Schema(name = "默认入站主题")
    private String inboundDefault = "/#";

    @Schema(name = "默认出站主题")
    private String outboundDefault = "/app/default/outbound";

    @Schema(name = "是否启用MQTT")
    private Boolean enabled = false;

}
