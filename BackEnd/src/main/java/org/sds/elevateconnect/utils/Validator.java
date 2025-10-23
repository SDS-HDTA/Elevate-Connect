package org.sds.elevateconnect.utils;

import org.sds.elevateconnect.model.project.FileType;
import org.springframework.web.multipart.MultipartFile;

import java.util.Objects;

public class Validator {
    public static boolean isValidFile(MultipartFile file, FileType fileType) {
        // Check file is not null
        if (file == null) return false;

        // Check if file has a valid extension for its fileType
        return switch (fileType) {
            case DOCUMENT: {
                String filename = file.getOriginalFilename();
                yield filename != null && filename.matches("(?i).*\\.(?:pdf|docx|doc)$");
            }
            case IMAGE: {
                String contentType = file.getContentType();
                yield contentType != null && contentType.startsWith("image");
            }
            case VIDEO: {
                String contentType = file.getContentType();
                yield contentType != null && contentType.startsWith("video");
            }
        };
    }
}
