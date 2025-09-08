package org.example.codesignconnect.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.codesignconnect.model.Post;

import java.sql.Timestamp;
import java.util.List;

@Data
@NoArgsConstructor
public class PostDetail {
    private Integer id;
    private Integer projectId;
    private Integer authorId;
    private String title;
    private String content;
    private Timestamp createTime;
    private List<ReplyDetail> replies;
    private String creatorName;

    public PostDetail(Post post, List<ReplyDetail> replies, String creatorName){
        this.projectId = post.getProjectId();
        this.authorId = post.getAuthorId();
        this.id = post.getId();
        this.title = post.getTitle();
        this.content = post.getContent();
        this.createTime = post.getCreateTime();
        this.replies = replies;
        this.creatorName = creatorName;
    }
}
