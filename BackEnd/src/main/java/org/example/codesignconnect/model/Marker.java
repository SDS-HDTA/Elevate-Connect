package org.example.codesignconnect.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Marker {
    private Integer id;
    private Integer projectId;
    private Double lat;
    private Double lng;
    private String title;
    private String description;
    private Timestamp createTime;
    private Timestamp updateTime;
}
