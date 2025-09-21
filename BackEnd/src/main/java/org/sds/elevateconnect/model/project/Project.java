package org.sds.elevateconnect.model.project;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Project {
    private Integer id;
    private Integer creatorId;
    private Integer projectImageId;
    private Integer communityId;
    private String name;
    private ProjectStage currentStage;
    private String description;
    private ProjectCategory category;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate targetDate;
    private Timestamp createTime;
}