package org.example.codesignconnect.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.example.codesignconnect.mapper.PostMapper;
import org.example.codesignconnect.mapper.ReplyMapper;
import org.example.codesignconnect.mapper.UserMapper;
import org.example.codesignconnect.model.Post;
import org.example.codesignconnect.model.Reply;
import org.example.codesignconnect.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
public class WebSocketMessageFactory {

    private static PostMapper postMapper;
    private static ReplyMapper replyMapper;
    private static UserMapper userMapper;

    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    public WebSocketMessageFactory(PostMapper postMapper, ReplyMapper replyMapper, UserMapper userMapper) {
        WebSocketMessageFactory.postMapper = postMapper;
        WebSocketMessageFactory.replyMapper = replyMapper;
        WebSocketMessageFactory.userMapper = userMapper;
    }

    public static String createNewPostMessage(Integer postId) {
        Post post = postMapper.getPostById(postId);
        if (post == null) return null;
        User author = userMapper.getUserById(post.getAuthorId());

        Map<String, Object> data = new HashMap<>();
        data.put("type", "new_post");

        Map<String, Object> postData = new HashMap<>();
        postData.put("id", post.getId());
        postData.put("content", post.getContent());
        postData.put("createTime", post.getCreateTime().toString());
        postData.put("senderName", author != null ? author.getUsername() : "unknown");
        postData.put("userId", post.getAuthorId());
        postData.put("messages", new Object[0]);

        data.put("post", postData);

        return toJson(data);
    }

    public static String createDeletePostMessage(Integer postId) {
        Map<String, Object> message = new HashMap<>();
        message.put("type", "delete_post");
        message.put("postId", postId.toString());
        return toJson(message);
    }

    public static String createNewReplyMessage(Integer replyId) {
        Reply reply = replyMapper.getReplyById(replyId);
        if (reply == null) return null;
        User author = userMapper.getUserById(reply.getAuthorId());

        Map<String, Object> data = new HashMap<>();
        data.put("type", "new_message");

        Map<String, Object> replyData = new HashMap<>();
        replyData.put("id", reply.getId());
        replyData.put("content", reply.getContent());
        replyData.put("createTime", reply.getCreateTime().toString());
        replyData.put("senderName", author != null ? author.getUsername() : "unknown");
        replyData.put("userId", reply.getAuthorId());

        data.put("message", replyData);
        data.put("postId", reply.getPostId().toString());

        return toJson(data);
    }

    public static String createDeleteReplyMessage(Integer replyId) {
        Map<String, Object> message = new HashMap<>();
        message.put("type", "delete_message");
        message.put("replyId", replyId.toString());
        return toJson(message);
    }

    private static String toJson(Object obj) {
        try {
            return objectMapper.writeValueAsString(obj);
        } catch (Exception e) {
            log.warn("Failed to serialize WebSocket message: {}", e.getMessage());
            return null;
        }
    }
}
