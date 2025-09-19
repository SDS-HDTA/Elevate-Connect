package org.sds.elevateconnect.dto;

import lombok.Data;

@Data
public class UserUpdateRequest {
    private Integer id;
    private String email;
    private String firstName;
    private String lastName;
}
