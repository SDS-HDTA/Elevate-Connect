package org.sds.elevateconnect.model.project;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

@Getter
public enum FileType {
    DOCUMENT(0),
    IMAGE(1),
    VIDEO(2);

    // Store a hashmap of intValue's to FileType enums
    private static final Map<Integer, FileType> fileTypeMap = new HashMap<>();

    // Populate the fileTypeMap hashmap
    static {
        for (FileType fileType: EnumSet.allOf(FileType.class)) {
            fileTypeMap.put(fileType.intValue, fileType);
        }
    }

    @JsonValue
    private final int intValue;

    FileType(int intValue) {
        this.intValue = intValue;
    }

    public static FileType fromInt(int intValue) {
        return fileTypeMap.get(intValue);
    }
}
