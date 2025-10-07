package org.sds.elevateconnect.controller;

import org.sds.elevateconnect.model.auth.Permission;
import org.sds.elevateconnect.model.auth.User;
import org.sds.elevateconnect.model.auth.UserRole;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/permissions")
public class PermissionsController {
    @GetMapping
    public ResponseEntity<Permission[]> getMapOfRoleToPermissions(@AuthenticationPrincipal User user) {
        Map<UserRole, Permission[]> rolePermissionMap = Permission.getRolePermissionsMap();
        return ResponseEntity.ok(rolePermissionMap.get(user.getRole()));
    }
}
