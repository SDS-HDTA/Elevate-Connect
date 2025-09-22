package org.sds.elevateconnect.model.project;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.sql.Date;
import java.sql.Timestamp;

@Data
@AllArgsConstructor
public class Iteration {
    private Integer id;
    private Integer projectId;
    private Short projectStatus;
    private Integer iteratedTime;
    private String title;
    private Date startDate;
    private Date endDate;
    private Timestamp createTime;
    private Timestamp updateTime;
}
