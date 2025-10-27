package org.sds.elevateconnect.model.project;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

@Getter
public enum ProjectCategory {
    EDUCATION(0, "Education"),
    WASH(1, "Water, Sanitation and Hygiene"),
    HEALTH(2, "Health"),
    NUTRITION(3, "Nutrition"),
    LIVELIHOODS(4, "Livelihoods"),
    GENDER_EQUALITY(5, "Gender Equality"),
    ENVIRONMENT_CLIMATE(6, "Environment and Climate"),
    SHELTER_HOUSING(7, "Shelter and Housing"),
    PROTECTION(8, "Protection"),
    GOVERNANCE_ADVOCACY(9, "Governance and Advocacy"),
    DRR(10, "Disaster Risk Reduction");

    // Store a hashmap of intValue's to ProjectCategory enums
    private static final Map<Integer, ProjectCategory> projectCategoryMap = new HashMap<>();

    // Populate the projectCategoryMap hashmap
    static {
        for (ProjectCategory category: EnumSet.allOf(ProjectCategory.class)) {
            projectCategoryMap.put(category.intValue, category);
        }
    }

    @JsonValue
    private final int intValue;
    private final String stringValue;

    ProjectCategory(int intValue, String stringValue) {
        this.intValue = intValue;
        this.stringValue = stringValue;
    }

    public static ProjectCategory fromInt(int intValue) {
        return projectCategoryMap.get(intValue);
    }
}
