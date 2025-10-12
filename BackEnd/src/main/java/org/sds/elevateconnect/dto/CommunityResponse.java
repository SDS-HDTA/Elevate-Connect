package org.sds.elevateconnect.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommunityResponse {
    private Integer id;
    private String name;
    private String country;
    private Integer memberCount;
    private Integer projectCount;
    private String shortDescription;
}
