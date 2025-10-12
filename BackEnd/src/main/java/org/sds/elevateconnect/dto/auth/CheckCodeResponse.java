package org.sds.elevateconnect.dto.auth;

import lombok.Data;

@Data
public class CheckCodeResponse {
    private Integer role;
    private Integer communityId;
    private String country;
    private String organization;
}
