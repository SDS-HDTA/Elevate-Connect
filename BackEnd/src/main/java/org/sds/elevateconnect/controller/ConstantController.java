package org.sds.elevateconnect.controller;

import org.sds.elevateconnect.model.auth.Permission;
import org.sds.elevateconnect.model.auth.User;
import org.sds.elevateconnect.model.auth.UserRole;
import org.sds.elevateconnect.model.project.MarkerType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

// Controller for fetching enums such as permissions, categories, etc.
@RestController
@RequestMapping("/constants")
public class ConstantController {
    @GetMapping("/permissions")
    public ResponseEntity<Permission[]> getMapOfRoleToPermissions(@AuthenticationPrincipal User user) {
        Map<UserRole, Permission[]> rolePermissionMap = Permission.getRolePermissionsMap();
        return ResponseEntity.ok(rolePermissionMap.get(user.getRole()));
    }

    @GetMapping("/marker-type")
    public ResponseEntity<Map<Integer, String>> getMarkerTypes() {
        Map<Integer, String> stringMarkerTypeMap = new HashMap<>();

        // Map the enum values to their string values
        MarkerType.markerTypeMap.forEach((intKey, markerType) -> stringMarkerTypeMap.put(intKey, markerType.getStringValue()));

        return ResponseEntity.ok(stringMarkerTypeMap);
    }
}
