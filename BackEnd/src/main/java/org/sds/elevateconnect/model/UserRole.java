package org.sds.elevateconnect.model;

import lombok.Getter;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonValue;

@Getter
public enum UserRole {
    COMMUNITY_INSIGHT_PARTNER(0, "Community Insight Partner"),
    COUNTRY_COLLABORATION_PARTNER(1, "Country Collaboration Partner"),
    HUMANITARIAN_IMPACT_PARTNER(2, "Humanitarian Impact Partner"),
    ELEVATE_FACILITATION_LEAD(3, "Elevate Facilitation Lead");

    // Store a hashmap of intValue's to UserRole enums
    private static final Map<Integer, UserRole> userRoleMap = new HashMap<Integer, UserRole>();

    // Populate the userRoleMap hashmap
    static {
        for (UserRole role: EnumSet.allOf(UserRole.class)) {
            userRoleMap.put(role.intValue, role);
        }
    }

   @JsonValue
    private final int intValue;

    private final String stringValue;

    UserRole(int intValue, String stringValue) {
        this.intValue = intValue;
        this.stringValue = stringValue;
    }

    public static UserRole fromInt(int intValue) {
        return userRoleMap.get(intValue);
    }
}
