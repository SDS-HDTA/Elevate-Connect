package org.sds.elevateconnect.service;

import org.sds.elevateconnect.mapper.ReplyMapper;
import org.sds.elevateconnect.model.Reply;
import org.sds.elevateconnect.service.interfaces.IReplyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReplyService implements IReplyService {

    @Autowired
    private ReplyMapper replyMapper;

    @Override
    public int addReply(Reply reply) {
        return replyMapper.insertReply(reply);
    }

    @Override
    public List<Reply> getRepliesByPostId(Integer postId) {
        return replyMapper.getRepliesByPostId(postId);
    }
}
