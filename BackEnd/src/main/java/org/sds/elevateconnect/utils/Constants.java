package org.sds.elevateconnect.utils;

import org.sds.elevateconnect.model.auth.Permission;
import org.sds.elevateconnect.model.auth.UserRole;
import org.sds.elevateconnect.model.project.ProjectStage;

import java.util.*;

public final class Constants {
    public static final List<String> UNAUTHORIZED_ENDPOINTS = Arrays.asList(
            "/admin/db/rebuild-database",
            "/admin/db/rebuild-with-sample-data",
            "/login",
            "/register",
            "/password/resetCode",
            "/password/update"
    );

    public static final String SESSION_USER = "user";

    public static final ProjectStage INITIAL_PROJECT_STAGE = ProjectStage.EMPATHISE;

    public static String getAuthorityAnnotation(Permission permission) {
        return "hasAuthority('" + permission.getValue() + "')";
    }

    // This map has been placed in Constants as it requires both Permission and UserRole enums to be initialised, which was causing errors
    public static final Map<UserRole, Permission[]> rolePermissionsMap = new HashMap<>();

    static {
        for (UserRole role : UserRole.values()) {
            rolePermissionsMap.put(role, role.getPermissions());
        }
    }
}
