package org.sds.elevateconnect.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.sds.elevateconnect.model.project.Reply;

import java.sql.Timestamp;

@Data
@NoArgsConstructor
public class ReplyDetail {
    private Integer id;
    private Integer postId;
    private Integer authorId;
    private String content;
    private Timestamp createTime;
    private String senderName;

    public ReplyDetail(Reply reply, String senderName){
        this.id = reply.getId();
        this.postId = reply.getPostId();
        this.authorId = reply.getAuthorId();
        this.content = reply.getContent();
        this.createTime = reply.getCreateTime();
        this.senderName = senderName;
    }
}
