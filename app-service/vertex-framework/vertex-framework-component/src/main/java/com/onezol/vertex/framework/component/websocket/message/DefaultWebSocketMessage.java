package com.onezol.vertex.framework.component.websocket.message;

import com.onezol.vertex.framework.common.util.JsonUtils;
import lombok.NonNull;
import org.springframework.web.socket.WebSocketMessage;

public record DefaultWebSocketMessage<T>(T payload) implements WebSocketMessage<T> {

    @Override
    @NonNull
    public T getPayload() {
        return payload;
    }

    @Override
    public int getPayloadLength() {
        return JsonUtils.toJsonString(payload).length();
    }

    @Override
    public boolean isLast() {
        return false;
    }

}
