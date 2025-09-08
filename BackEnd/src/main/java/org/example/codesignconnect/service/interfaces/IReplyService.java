package org.example.codesignconnect.service.interfaces;

import org.example.codesignconnect.model.Reply;

import java.util.List;

public interface IReplyService {
    int addReply(Reply reply);
    List<Reply> getRepliesByPostId(Integer postId);
}
