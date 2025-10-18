package org.sds.elevateconnect.dto;

import lombok.Builder;
import lombok.Data;
import org.sds.elevateconnect.model.project.File;
import org.springframework.web.multipart.MultipartFile;

@Data
@Builder
public class GcsFileRequest {
    private File file;
    private Integer projectId;
    private MultipartFile multipartFile;
}
