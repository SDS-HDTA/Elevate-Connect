package org.sds.elevateconnect.model;

//import org.springframework.security.core.GrantedAuthority;
//import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

public class SecurityUserDetails  { // implements UserDetails
    private final User user;

    public SecurityUserDetails(User user) {
        this.user = user;
    }

//    @Override
//    public Collection<? extends GrantedAuthority> getAuthorities() {
//        Permission[] rolePermissions = Permission.rolePermissionsMap.get(user.getRole());
//        return List.of(rolePermissions);
//    }
//
//    @Override
//    public String getPassword() {
//        return user.getPassword();
//    }
//
//    @Override
//    public String getUsername() {
//        return user.getEmail();
//    }
}
