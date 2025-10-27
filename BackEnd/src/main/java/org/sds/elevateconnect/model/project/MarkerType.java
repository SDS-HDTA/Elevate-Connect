package org.sds.elevateconnect.model.project;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

@Getter
public enum MarkerType {
    HEALTH_CLINIC(0, "Health Clinic"),
    COMMUNITY_HOMESTAY(1, "Community Homestay"),
    HERITAGE_SITE(2, "Heritage Site"),
    RELIGIOUS_SITE(3, "Religious Site"),
    WASTE_MANAGEMENT_SITE(4, "Waste Management Site"),
    WATER_SOURCE_DISTRIBUTION_POINT(5, "Water Source Distribution Point"),
    POWER_FACILITY(6, "Power Facility"),
    TELECOM_FACILITY(7, "Telecom Facility"),
    RESIDENTIAL_HOME(8, "Residential Home"),
    COMMUNITY_CENTER(9, "Community Center"),
    EDUCATION_SITE(10, "Education Site"),
    MARKETPLACE(11, "Marketplace"),
    FOOD_DISTRIBUTION(12, "Food Distribution"),
    TRANSPORT_HUB(13, "Transport Hub"),
    HAZARD(14, "Hazard");

    // Store a hashmap of intValue's to MarkerType enums
    public static final Map<Integer, MarkerType> markerTypeMap = new HashMap<>();

    // Populate the markerTypeMap hashmap
    static {
        for (MarkerType markerType: EnumSet.allOf(MarkerType.class)) {
            markerTypeMap.put(markerType.intValue, markerType);
        }
    }

    @JsonValue
    private final int intValue;
    private final String stringValue;

    MarkerType(int intValue, String stringValue) {
        this.intValue = intValue;
        this.stringValue = stringValue;
    }

    public static MarkerType fromInt(int intValue) {
        return markerTypeMap.get(intValue);
    }
}
