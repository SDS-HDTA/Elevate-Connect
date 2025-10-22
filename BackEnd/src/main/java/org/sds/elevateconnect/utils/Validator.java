package org.sds.elevateconnect.utils;

import org.sds.elevateconnect.model.project.FileType;
import org.springframework.web.multipart.MultipartFile;

import java.util.Objects;

public class Validator {
    public static boolean isValidFile(MultipartFile file, FileType fileType) {
        // Check file is not null
        Objects.requireNonNull(file);

        // Check if file has a valid extension for its fileType
        return switch (fileType) {
            case DOCUMENT -> Objects.requireNonNull(file.getOriginalFilename()).matches("(?i).*\\.(?:pdf|docx|doc)$");
            case IMAGE -> Objects.requireNonNull(file.getContentType()).startsWith("image");
            case VIDEO -> Objects.requireNonNull(file.getContentType()).startsWith("video");
        };
    }
}
