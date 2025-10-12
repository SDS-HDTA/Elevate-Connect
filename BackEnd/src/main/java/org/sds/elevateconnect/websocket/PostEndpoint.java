package org.sds.elevateconnect.websocket;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpSession;
import jakarta.websocket.*;
import jakarta.websocket.server.ServerEndpoint;
import lombok.extern.slf4j.Slf4j;
import org.sds.elevateconnect.config.GetHTTPSessionConfig;
import org.sds.elevateconnect.model.project.Message;
import org.sds.elevateconnect.utils.Constants;
import org.sds.elevateconnect.utils.MessageUtils;
import org.sds.elevateconnect.utils.WebsocketUtils;
import org.springframework.stereotype.Component;

@Slf4j
@ServerEndpoint(value = "/projects/{projectId}/post", configurator = GetHTTPSessionConfig.class)
@Component
public class PostEndpoint {
    private static final ObjectMapper objectMapper = new ObjectMapper();
    private String path;
    private String user;

    @OnOpen
    public void onOpen(Session session, EndpointConfig config) {
        try {
            HttpSession httpSession = (HttpSession) config.getUserProperties().get(HttpSession.class.getName());
            this.user = (String) httpSession.getAttribute(Constants.SESSION_USER);
            this.path = session.getRequestURI().getPath();
            WebsocketUtils.onUserConnected(this.path, this.user, session);
        } catch (Exception e) {
            WebsocketUtils.printErrorMsg(e);
        }
    }

    @OnMessage
    public void onMessage(Session session, String message) {
        try {
            Message msgFrom = objectMapper.readValue(message, Message.class);
            String broadcastMsg = MessageUtils.getMessage(true, null, msgFrom.getMessage());
            WebsocketUtils.broadcast(this.path, broadcastMsg);
        } catch (Exception e) {
            WebsocketUtils.printErrorMsg(e);
        }
    }

    @OnClose
    public void onClose(Session session) {
        try {
            WebsocketUtils.onUserDisconnected(this.path, this.user);
        } catch (Exception e) {
            WebsocketUtils.printErrorMsg(e);
        }
    }
}
