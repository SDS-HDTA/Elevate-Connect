package org.sds.elevateconnect.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SignupRequest {
    private String email;
    private String username;
    private String password;
    private String inviteCode;
}
