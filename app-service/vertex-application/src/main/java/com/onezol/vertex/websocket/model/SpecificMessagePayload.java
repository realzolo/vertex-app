package com.onezol.vertex.websocket.model;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class SpecificMessagePayload {

    private Long toUserId;

    /**
     * 内容
     */
    private String body;

}
