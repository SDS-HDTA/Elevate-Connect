package org.sds.elevateconnect.model.project;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Marker {
    private Integer id;
    private Integer projectId;
    private Double lat;
    private Double lng;
    private String title;
    private String description;
    private MarkerType type;
}
