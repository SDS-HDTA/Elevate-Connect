package org.sds.elevateconnect.dto.auth;

import lombok.Data;

@Data
public class CheckCodeRequest {
    private String email;
    private String code;
}

