package org.example.codesignconnect.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpSession;
import jakarta.websocket.*;
import jakarta.websocket.server.PathParam;
import jakarta.websocket.server.ServerEndpoint;
import lombok.extern.slf4j.Slf4j;
import org.example.codesignconnect.config.GetHTTPSessionConfig;
import org.example.codesignconnect.model.Message;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@ServerEndpoint(value = "/projects/{projectId}/channel", configurator = GetHTTPSessionConfig.class)
@Component
public class ChatEndpoint {
    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static final Map<String, Map<String, Session> > onlineUsers = new ConcurrentHashMap<>();
    private HttpSession httpSession;

    @OnOpen
    public void onOpen(Session session, EndpointConfig config, @PathParam("projectId") String projectId) {
        log.info("onOpen: {}", session.getId());
        try {
            this.httpSession = (HttpSession) config.getUserProperties().get(HttpSession.class.getName());
            String user = (String) this.httpSession.getAttribute("user");
            onlineUsers.computeIfAbsent(projectId, k -> new ConcurrentHashMap<>()).put(user, session);
            String message = MessageUtils.getMessage(true,null, getFriends(projectId));
            //broadcastAllUsers(projectId, message);
        } catch (Exception e) {
            log.error("Error: {}", e.getMessage());
        }
    }

    public Set<String> getFriends(String projectId) {
        Map<String, Session> projectSessions = onlineUsers.get(projectId);
        return projectSessions != null ? projectSessions.keySet() : Collections.emptySet();
    }

    private void broadcastAllUsers(String projectId, String message) {
        try {
            Map<String, Session> projectSessions = onlineUsers.get(projectId);
            if (projectSessions != null) {
                for (Session session : projectSessions.values()) {
                    session.getBasicRemote().sendText(message);
                }
            }
        } catch (Exception e) {
            log.error("Error: {}", e.getMessage());
        }
    }

    @OnMessage
    public void onMessage(String message, @PathParam("projectId") String projectId) {
        try {
            Message msgFrom = objectMapper.readValue(message, Message.class);
            String toName = msgFrom.getToName();
            String messageTemp = msgFrom.getMessage();
            if(toName == null){
                broadcastAllUsers(projectId, MessageUtils.getMessage(true, null, messageTemp));
                return;
            }
            Map<String, Session> projectSessions = onlineUsers.get(projectId);
            if (projectSessions != null) {
                Session session = projectSessions.get(toName);
                if (session != null) {
                    String user = (String) this.httpSession.getAttribute("user");
                    String msgTo = MessageUtils.getMessage(false, user, messageTemp);
                    session.getBasicRemote().sendText(msgTo);
                }
            }
        } catch (Exception e) {
            log.error("Error: {}", e.getMessage());
        }
    }

    @OnClose
    public void onClose(Session session, @PathParam("projectId") String projectId) {
        try {
            String user = (String) this.httpSession.getAttribute("user");
            Map<String, Session> projectSessions = onlineUsers.get(projectId);
            if(projectSessions != null) {
                projectSessions.remove(user);
                String message = MessageUtils.getMessage(true,null, getFriends(projectId));
                broadcastAllUsers(projectId, message);
            }
        } catch (Exception e) {
            log.error("Error: {}", e.getMessage());
        }
    }
}
