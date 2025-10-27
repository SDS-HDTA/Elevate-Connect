package org.sds.elevateconnect.dto;

import lombok.Data;
import org.sds.elevateconnect.model.project.MarkerType;

@Data
public class CreateMarkerRequest {
    private Integer projectId;
    private Double lat;
    private Double lng;
    private String title;
    private String description;
    private Integer type;
}
