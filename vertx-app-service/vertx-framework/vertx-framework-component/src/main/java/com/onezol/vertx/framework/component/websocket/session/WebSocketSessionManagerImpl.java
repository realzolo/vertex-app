package com.onezol.vertx.framework.component.websocket.session;

import org.springframework.web.socket.WebSocketSession;

import java.util.Collection;
import java.util.Collections;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * 默认的 {@link WebSocketSessionManager} 实现类
 */
public class WebSocketSessionManagerImpl implements WebSocketSessionManager {

    /**
     * id 与 WebSocketSession 映射
     * <p>
     * key：Session 编号
     */
    private final ConcurrentMap<String, WebSocketSession> idSessions = new ConcurrentHashMap<>();

    /**
     * user 与 WebSocketSession 映射
     * <p>
     * key1：用户类型
     * key2：用户编号
     */
    private final ConcurrentMap<Integer, ConcurrentMap<Long, CopyOnWriteArrayList<WebSocketSession>>> userSessions
            = new ConcurrentHashMap<>();

    @Override
    public void addSession(WebSocketSession session) {
        System.out.println("添加 WebSocketSession：" + session);
    }

    @Override
    public void removeSession(WebSocketSession session) {
        System.out.println("移除 WebSocketSession：" + session);
    }

    @Override
    public WebSocketSession getSession(String id) {
        return idSessions.get(id);
    }

    @Override
    public Collection<WebSocketSession> getSessionList(Integer userType) {
        System.out.println("获取用户类型为 " + userType);
        return Collections.emptyList();
    }

    @Override
    public Collection<WebSocketSession> getSessionList(Integer userType, Long userId) {
        System.out.println("获取用户类型为 " + userType + "，用户编号为 " + userId);
        return Collections.emptyList();
    }

}
