package org.sds.elevateconnect.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    private Integer id;
    private String firstName;
    private String lastName;
    private String email;
//    private String phone;
    private String password;
    private UserRole role;
    private LocalDateTime createTime;

    public String getFullName() {
        return firstName + ' ' + lastName;
    }
}
