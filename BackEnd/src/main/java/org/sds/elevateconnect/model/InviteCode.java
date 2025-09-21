package org.sds.elevateconnect.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class InviteCode {
    private Integer id;
    private Integer communityId;
    private String email;
    private String code;
    private UserRole userRole;
    private String country;
    private LocalDateTime createTime;
}
