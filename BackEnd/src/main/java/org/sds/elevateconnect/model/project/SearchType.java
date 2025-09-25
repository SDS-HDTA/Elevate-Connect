package org.sds.elevateconnect.model.project;

import com.fasterxml.jackson.annotation.JsonValue;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

public enum SearchType {
    NAME(0),
    CATEGORY(1),
    COUNTRY(2);

    // Store a hashmap of intValue's to UserRole enums
    private static final Map<Integer, SearchType> searchTypeMap = new HashMap<>();

    // Populate the userRoleMap hashmap
    static {
        for (SearchType searchType: EnumSet.allOf(SearchType.class)) {
            searchTypeMap.put(searchType.intValue, searchType);
        }
    }

    @JsonValue
    private final int intValue;

    SearchType(int intValue) {
        this.intValue = intValue;
    }

    public static SearchType fromInt(int intValue) {
        return searchTypeMap.get(intValue);
    }
}
