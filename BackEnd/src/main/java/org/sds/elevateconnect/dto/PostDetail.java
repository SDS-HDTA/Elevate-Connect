package org.sds.elevateconnect.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.sds.elevateconnect.model.project.Post;

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
        this.id = post.getId();
        this.projectId = post.getProjectId();
        this.authorId = post.getAuthorId();
        this.title = post.getTitle();
        this.content = post.getContent();
        this.createTime = post.getCreateTime();
        this.replies = replies;
        this.creatorName = creatorName;
    }
}
