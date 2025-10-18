package org.sds.elevateconnect.model.project;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class File {
    private Integer id;
    private Integer iterationId;
    private Integer creatorId;
    private FileType type;
    private String name;
    private String source;
    private Timestamp createTime;
}
