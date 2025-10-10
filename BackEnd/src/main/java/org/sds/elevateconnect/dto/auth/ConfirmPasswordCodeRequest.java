package org.sds.elevateconnect.dto.auth;

import lombok.Data;

@Data
public class ConfirmPasswordCodeRequest {
    private String email;
    private String verificationCode;
}
