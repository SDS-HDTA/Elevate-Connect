package org.sds.elevateconnect.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.sds.elevateconnect.model.project.Project;
import org.sds.elevateconnect.model.User;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProjectDetail {
    private Project project;
    private List<User> members;
    private String creatorName;
}
