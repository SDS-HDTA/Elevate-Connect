package org.sds.elevateconnect.dto;

import lombok.Data;

@Data
public class CreateMarkerRequest {
    private Integer projectId;
    private Double lat;
    private Double lng;
    private String title;
    private String description;
    private Integer type;
}
