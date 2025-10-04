package org.sds.elevateconnect.model.auth;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

import static org.sds.elevateconnect.utils.Constants.rolePermissionsMap;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User implements UserDetails {
    private Integer id;
    private Integer communityId;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private UserRole role;
    private String country;
    private String organisation;
    private LocalDateTime createTime;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Permission[] rolePermissions = rolePermissionsMap.get(role);
        return List.of(rolePermissions);
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public String getPassword() {
        return password;
    }
}
