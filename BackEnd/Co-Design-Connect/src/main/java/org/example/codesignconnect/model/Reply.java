package org.example.codesignconnect.model;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class Reply {
    private Integer id;
    private Integer postId;
    private Integer authorId;
    private Integer channelId;
    private String content;
    private Timestamp createTime;
    private Timestamp updateTime;
}
