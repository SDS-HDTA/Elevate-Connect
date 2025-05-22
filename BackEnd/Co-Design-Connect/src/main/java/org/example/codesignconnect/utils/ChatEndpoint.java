package org.example.codesignconnect.utils;

import jakarta.servlet.http.HttpSession;
import jakarta.websocket.*;
import jakarta.websocket.server.ServerEndpoint;
import lombok.extern.slf4j.Slf4j;
import org.example.codesignconnect.model.Message;
import com.alibaba.fastjson.JSON;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@ServerEndpoint("/chat")
public class ChatEndpoint {
    private static final Map<String, Session> onlineUsers = new ConcurrentHashMap<>();
    private HttpSession httpSession;

    @OnOpen
    public void onOpen(Session session, EndpointConfig config) {
        this.httpSession = (HttpSession) config.getUserProperties().get(HttpSession.class.getName());
        String user = (String) this.httpSession.getAttribute("user");
        onlineUsers.put(user,session);
        String message = MessageUtils.getMessage(true,null, getFriends());
        broadcastAllUsers(message);
    }


    public Set<String> getFriends() {
        return onlineUsers.keySet();
    }

    private void broadcastAllUsers(String message) {
        try {
            Set<Map.Entry<String, Session>> entries = onlineUsers.entrySet();
            for (Map.Entry<String, Session> entry : entries) {
                Session session = entry.getValue();
                session.getBasicRemote().sendText(message);
            }
        } catch (Exception e) {
            log.error("Error: ", e);
        }
    }

    @OnMessage
    public void onMessage(String message) {
        try {
            Message msg = JSON.parseObject(message, Message.class);
            String toName = msg.getToName();
            String messageTemp = msg.getMessage();
            Session session = onlineUsers.get(toName);
            String user = (String) this.httpSession.getAttribute("user");
            String msg1 = MessageUtils.getMessage(false, user, messageTemp);
            session.getBasicRemote().sendText(msg1);
        } catch (Exception e) {
            log.error("Error: ", e);
        }
    }

    @OnClose
    public void onClose(Session session) {
        String user = (String) this.httpSession.getAttribute("user");
        onlineUsers.remove(user);
        String message = MessageUtils.getMessage(true,null, getFriends());
        broadcastAllUsers(message);
    }
}
