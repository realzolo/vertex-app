package com.onezol.vertx.websocket.model;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class RedisMessagePayload {

    private String stream;

    private String group;

}
