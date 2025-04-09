package com.onezol.vertex.framework.support.config.properties;

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

    @Schema(description = "MQTT Broker URL")
    private String brokerUrl;

    @Schema(description = "MQTT Client ID")
    private String clientId;

    @Schema(description = "MQTT Username")
    private String username;

    @Schema(description = "MQTT Password")
    private String password;

    @Schema(description = "默认入站主题")
    private String inboundDefault = "/#";

    @Schema(description = "默认出站主题")
    private String outboundDefault = "/app/default/outbound";

    @Schema(description = "是否启用MQTT")
    private Boolean enabled = false;

}
