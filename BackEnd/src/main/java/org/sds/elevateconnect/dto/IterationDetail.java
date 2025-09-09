package org.sds.elevateconnect.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.sds.elevateconnect.model.Iteration;

import java.sql.Timestamp;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class IterationDetail {
    private Integer id;
    private String title;
    private List<TaskDetail> tasks;
    private Timestamp createTime;
    private Timestamp updateTime;

    public IterationDetail(List<TaskDetail> tasks, Iteration iteration){
        this.id = iteration.getId();
        this.title = iteration.getTitle();
        this.tasks = tasks;
        this.createTime = iteration.getCreateTime();
        this.updateTime = iteration.getUpdateTime();
    }
}
