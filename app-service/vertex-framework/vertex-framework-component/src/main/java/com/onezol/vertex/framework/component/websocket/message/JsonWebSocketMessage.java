package com.onezol.vertex.framework.component.websocket.message;

import lombok.Data;

import java.io.Serializable;

/**
 * JSON 格式的 WebSocket 消息帧
 */
@Data
public class JsonWebSocketMessage implements Serializable {

    /**
     * 消息组
     */
    private String group;

    /**
     * 消息内容
     * <p>
     * 要求 JSON 对象
     */
    private String content;

}
