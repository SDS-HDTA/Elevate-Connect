package org.example.codesignconnect.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.sql.Timestamp;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class Project {
    private Integer id;
    private String name;
    private Integer creatorId;
    private Integer status;
    private String description;
    private String imageUrl;
    private Integer channelId;
    private String category;
    private Date deadline;
    private String tags;
    private String area;
    private Timestamp createTime;
    private Timestamp updateTime;
}