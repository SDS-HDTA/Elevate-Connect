package org.sds.elevateconnect.service.interfaces;

import org.sds.elevateconnect.model.project.Reply;

import java.util.List;

public interface IReplyService {
    int addReply(Reply reply);
    List<Reply> getRepliesByPostId(Integer postId);
}
