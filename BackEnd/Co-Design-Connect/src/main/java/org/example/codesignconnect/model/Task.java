package org.example.codesignconnect.model;

import lombok.Data;
import java.sql.Timestamp;

@Data
public class Task {
    private Integer id;
    private Integer taskId;
    private Integer projectId;
    private Integer iterationId;
    private String code;
    private String content;
    private Byte status;
    private Byte projectStatus;
    private Integer creatorId;
    private Integer assigneeId;
    private Timestamp createdTime;
    private Timestamp updatedTime;
}
