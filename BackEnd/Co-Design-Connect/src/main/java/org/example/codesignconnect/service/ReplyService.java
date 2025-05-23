package org.example.codesignconnect.service;

import org.example.codesignconnect.model.Reply;

import java.util.List;

public interface ReplyService {
    int createReply(Reply reply);
    List<Reply> getRepliesByPostId(Integer postId);
    boolean deleteReply(Integer replyId);
}
