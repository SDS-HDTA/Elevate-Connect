package org.example.codesignconnect.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.codesignconnect.model.Project;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProjectDetail {
    private Project project;
    private String creatorName;
}
