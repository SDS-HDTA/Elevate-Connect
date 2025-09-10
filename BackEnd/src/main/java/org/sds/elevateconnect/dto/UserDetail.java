package org.sds.elevateconnect.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.sds.elevateconnect.model.User;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
// Purpose of this class is to map UserRole enum to int value for responses
// This can potentially be implemented better tbh
public class UserDetail {
    private Integer id;
    private String firstName;
    private String lastName;
    private String email;
    //    private String phone;
    private String password;
    private int role;
    private LocalDateTime createTime;

    public UserDetail(User user){
        this.id = user.getId();
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
        this.email = user.getEmail();
        this.password = user.getPassword();
        this.role = user.getRole().getIntValue();
        this.createTime = user.getCreateTime();
    }
}
