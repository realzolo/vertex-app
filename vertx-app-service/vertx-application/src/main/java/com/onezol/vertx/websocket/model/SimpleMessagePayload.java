package com.onezol.vertx.websocket.model;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class SimpleMessagePayload {

    /**
     * 发送给谁
     * <p>
     * 如果为空，说明发送给所有人
     */
    private Long to;

    /**
     * 内容
     */
    private String content;

}
