package org.sds.elevateconnect.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CreateProjectRequest {
    private Integer creatorId;
    private Integer communityId;
    private String name;
    private String description;
    private Integer category;
    private String targetDate;
}
