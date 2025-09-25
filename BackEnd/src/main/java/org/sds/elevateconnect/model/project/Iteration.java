package org.sds.elevateconnect.model.project;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.sql.Timestamp;
import java.time.LocalDate;

@Data
@AllArgsConstructor
public class Iteration {
    private Integer id;
    private Integer projectId;
    private Integer projectStatus;
    private Integer iteratedTime;
    private String title;
    private LocalDate startDate;
    private LocalDate endDate;
    private Timestamp createTime;
    private Timestamp updateTime;
}
