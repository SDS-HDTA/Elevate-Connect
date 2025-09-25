package org.sds.elevateconnect.model.project;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

@Getter
public enum ProjectCategory {
    // TODO: Replace with real categories from client
    CATEGORY0(0, "CATEGORY0"),
    CATEGORY1(1, "CATEGORY1"),
    CATEGORY2(2, "CATEGORY2"),
    CATEGORY3(3, "CATEGORY3"),
    CATEGORY4(4, "CATEGORY4"),
    CATEGORY5(5, "CATEGORY5");

    // Store a hashmap of intValue's to UserRole enums
    private static final Map<Integer, ProjectCategory> projectCategoryMap = new HashMap<>();

    // Populate the userRoleMap hashmap
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
