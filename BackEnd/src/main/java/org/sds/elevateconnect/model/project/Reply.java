package org.sds.elevateconnect.model.project;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Reply {
    private Integer id;
    private Integer postId;
    private Integer authorId;
    private String content;
    private Timestamp createTime;
}
