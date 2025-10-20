package org.sds.elevateconnect.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class FileUploadRequest {
    private Integer iterationId;
    private Integer creatorId;
    private Integer type;
}