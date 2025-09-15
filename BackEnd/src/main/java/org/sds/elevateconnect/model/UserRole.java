package org.sds.elevateconnect.model;

import lombok.Getter;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonValue;

import static org.sds.elevateconnect.model.Permission.*;

@Getter
public enum UserRole {
    COMMUNITY_INSIGHT_PARTNER(0, "Community Insight Partner", communityInsightPartnerPermissions),
    COUNTRY_COLLABORATION_PARTNER(1, "Country Collaboration Partner", communityInsightPartnerPermissions),
    HUMANITARIAN_IMPACT_PARTNER(2, "Humanitarian Impact Partner", humanitarianImpactPartnerPermissions),
    ELEVATE_FACILITATION_LEAD(3, "Elevate Facilitation Lead", elevateFacilitationLeadPermissions);

    // Store a hashmap of intValue's to UserRole enums
    private static final Map<Integer, UserRole> userRoleMap = new HashMap<>();

    // Populate the userRoleMap hashmap
    static {
        for (UserRole role: EnumSet.allOf(UserRole.class)) {
            userRoleMap.put(role.intValue, role);
        }
    }

   @JsonValue
    private final int intValue;
    private final String stringValue;
    private final Permission[] permissions;

    UserRole(int intValue, String stringValue, Permission[] permissions) {
        this.intValue = intValue;
        this.stringValue = stringValue;
        this.permissions = permissions;
    }

    public static UserRole fromInt(int intValue) {
        return userRoleMap.get(intValue);
    }
}
