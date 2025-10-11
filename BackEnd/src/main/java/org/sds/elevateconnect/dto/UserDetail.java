package org.sds.elevateconnect.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.sds.elevateconnect.model.auth.User;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class UserDetail {
    private Integer id;
    private Integer communityId;
    private String firstName;
    private String lastName;
    private String email;
    private int role;
    private String country;
    private String phone;
    private String organization;
    private LocalDateTime createTime;

    public UserDetail(User user){
        this.id = user.getId();
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
        this.email = user.getEmail();
        this.role = user.getRole().getIntValue();
        this.communityId = user.getCommunityId();
        this.country = user.getCountry();
        this.phone = user.getPhone();
        this.organization = user.getOrganization();
        this.createTime = user.getCreateTime();
    }
}
