package org.sds.elevateconnect.utils;

import org.sds.elevateconnect.model.Permission;
import org.sds.elevateconnect.model.UserRole;

import java.util.HashMap;
import java.util.Map;

public final class Constants {
    public static final String SESSION_USER = "user";

    public static String getAuthorityAnnotation(Permission permission) {
        return "hasAuthority('" + permission.getValue() + "')";
    }

    public static final String PERMISSION_CREATE_POST = "create:post";

    public static final Map<UserRole, Permission[]> rolePermissionsMap = new HashMap<>();

    static {
        for (UserRole role : UserRole.values()) {
            rolePermissionsMap.put(role, role.getPermissions());
        }
    }
}
