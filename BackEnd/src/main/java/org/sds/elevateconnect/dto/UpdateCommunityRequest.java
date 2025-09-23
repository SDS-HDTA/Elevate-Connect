package org.sds.elevateconnect.dto;

import lombok.Data;

@Data
public class UpdateCommunityRequest {
    private Integer id;
    private String name;
    private String country;
    private String shortDescription;
}
