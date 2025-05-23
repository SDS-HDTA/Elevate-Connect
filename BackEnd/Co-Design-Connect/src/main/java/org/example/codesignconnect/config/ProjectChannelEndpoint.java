package org.example.codesignconnect.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.websocket.*;
import jakarta.websocket.server.PathParam;
import jakarta.websocket.server.ServerEndpoint;
import lombok.extern.slf4j.Slf4j;
import org.example.codesignconnect.model.WebSocketMessage;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Component
@ServerEndpoint("/projects/{projectId}/channel")
public class ProjectChannelEndpoint {

    private static final Map<Integer, Map<Session, String>> projectSessions = new ConcurrentHashMap<>();

    @OnOpen
    public void onOpen(Session session, @PathParam("projectId") Integer projectId) {
        projectSessions.putIfAbsent(projectId, new ConcurrentHashMap<>());
        projectSessions.get(projectId).put(session, session.getId());
        log.info("WebSocket connected: session {} joined project {}", session.getId(), projectId);
    }

    @OnMessage
    public void onMessage(String message, Session session, @PathParam("projectId") Integer projectId) {
        log.info("Received message from session {}: {}", session.getId(), message);
    }

    @OnClose
    public void onClose(Session session, @PathParam("projectId") Integer projectId) {
        Map<Session, String> sessions = projectSessions.get(projectId);
        if (sessions != null) {
            sessions.remove(session);
            log.info("WebSocket disconnected: session {} left project {}", session.getId(), projectId);
        }
    }

    @OnError
    public void onError(Session session, Throwable error) {
        log.error("WebSocket error: {}", error.getMessage());
    }

    public static void sendToProject(Integer projectId, String jsonMessage) {
        Map<Session, String> sessions = projectSessions.get(projectId);
        if (sessions != null) {
            for (Session session : sessions.keySet()) {
                try {
                    session.getBasicRemote().sendText(jsonMessage);
                } catch (IOException e) {
                    log.warn("Failed to send message to session {}: {}", session.getId(), e.getMessage());
                }
            }
        }
    }
    public static void broadcastToProject(Integer projectId, WebSocketMessage message) {
        Map<Session, String> sessions = projectSessions.get(projectId);
        if (sessions != null) {
            sessions.keySet().forEach(session -> {
                if (session.isOpen()) {
                    try {
                        session.getBasicRemote().sendText(new ObjectMapper().writeValueAsString(message));
                    } catch (IOException e) {
                        log.error("WebSocket send error", e);
                    }
                }
            });
        }
    }

}
