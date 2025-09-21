package org.sds.elevateconnect.model.project;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

@Getter
public enum ProjectStage {
    EMPATHISE(0, "Empathise"),
    DISCOVER(1, "Discover"),
    DEFINE(2, "Define"),
    IDEATE(3, "Ideate"),
    PROTOTYPE(4, "Prototype"),
    COMPLETED(5, "Completed");

    // Store a hashmap of intValue's to UserRole enums
    private static final Map<Integer, ProjectStage> projectStageMap = new HashMap<>();

    // Populate the userRoleMap hashmap
    static {
        for (ProjectStage stage: EnumSet.allOf(ProjectStage.class)) {
            projectStageMap.put(stage.intValue, stage);
        }
    }

    @JsonValue
    private final int intValue;
    private final String stringValue;

    ProjectStage(int intValue, String stringValue) {
        this.intValue = intValue;
        this.stringValue = stringValue;
    }

    public static ProjectStage fromInt(int intValue) {
        return projectStageMap.get(intValue);
    }
}
