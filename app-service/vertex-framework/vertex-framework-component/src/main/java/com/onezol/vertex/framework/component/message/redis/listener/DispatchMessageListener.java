package com.onezol.vertex.framework.component.message.redis.listener;

import com.onezol.vertex.framework.common.model.ObjectMethodMapping;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.stream.MapRecord;
import org.springframework.data.redis.connection.stream.RecordId;
import org.springframework.data.redis.stream.StreamListener;
import org.springframework.stereotype.Component;

import java.util.*;

@Slf4j
@Component
public class DispatchMessageListener implements StreamListener<String, MapRecord<String, String, String>> {

    private final Map<String, List<ObjectMethodMapping>> listeners = new HashMap<>();

    public void setListeners(Map<String, List<ObjectMethodMapping>> streamListeners) {
        Set<Map.Entry<String, List<ObjectMethodMapping>>> entries = streamListeners.entrySet();
        for (Map.Entry<String, List<ObjectMethodMapping>> entry : entries) {
            String streamKey = entry.getKey().split(":")[0];
            List<ObjectMethodMapping> mappings = listeners.getOrDefault(streamKey, new ArrayList<>());
            mappings.addAll(entry.getValue());
            listeners.put(streamKey, mappings);
        }
        listeners.putAll(streamListeners);
    }

    @Override
    public void onMessage(MapRecord<String, String, String> mapRecord) {
        String stream = mapRecord.getStream();
        RecordId recordId = mapRecord.getId();
        Map<String, String> payload = mapRecord.getValue();

        List<ObjectMethodMapping> streamListeners = listeners.get(stream);
        if (streamListeners == null) {
            log.info("[redis]未找到对应的监听器，streamKey={}", stream);
            return;
        }
        System.out.println("收到【Redis】消息： streamKey=" + stream + ", recordId=" + recordId + ", msg=" + payload);
        for (ObjectMethodMapping listener : streamListeners) {
            try {
                listener.getMethod().invoke(listener.getObject(), mapRecord);
            } catch (Exception e) {
                log.error("[redis]消息处理失败，streamKey={}, recordId={}, msg={}", stream, recordId, payload, e);
            }
        }
    }
}
