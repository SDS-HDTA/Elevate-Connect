package org.example.codesignconnect.model;

import lombok.Data;
import java.sql.Date;
import java.sql.Timestamp;

@Data
public class Iteration {
    private Integer id;
    private Integer projectId;
    private Byte projectStatus;
    private Integer iteratedTime;
    private String title;
    private Date startDate;
    private Date endDate;
    private Timestamp createdTime;
    private Timestamp updatedTime;
}
