package org.sds.elevateconnect.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.sds.elevateconnect.model.Community;
import org.sds.elevateconnect.model.project.Project;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProjectResponse {
    private Project project;
    private List<UserDetail> members;
    private Community community;
}
