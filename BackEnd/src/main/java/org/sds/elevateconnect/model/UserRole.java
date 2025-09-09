package org.sds.elevateconnect.model;

import lombok.Getter;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

@Getter
public enum UserRole {
    COMMUNITY_INSIGHT_PARTNER(0),
    COUNTRY_COLLABORATION_PARTNER(1),
    HUMANITARIAN_IMPACT_PARTNER(2),
    ELEVATE_FACILITATION_LEAD(3);

    // Store a hashmap of intValue's to UserRole enums
    private static final Map<Integer, UserRole> userRoleMap = new HashMap<Integer, UserRole>();

    // Populate the userRoleMap hashmap
    static {
        for (UserRole role: EnumSet.allOf(UserRole.class)) {
            userRoleMap.put(role.intValue, role);
        }
    }

    final int intValue;

    UserRole(int intValue) { this.intValue = intValue; }

    public static UserRole fromInt(int intValue) {
        return userRoleMap.get(intValue);
    }
}
