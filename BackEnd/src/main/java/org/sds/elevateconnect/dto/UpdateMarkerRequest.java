package org.sds.elevateconnect.dto;

import lombok.Data;
import org.sds.elevateconnect.model.project.MarkerType;

@Data
public class UpdateMarkerRequest {
    private Integer id;
    private String title;
    private Double lat;
    private Double lng;
    private String description;
    private MarkerType type;
}
