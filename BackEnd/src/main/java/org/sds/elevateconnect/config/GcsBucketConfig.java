package org.sds.elevateconnect.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "gcs")
public class GcsBucketConfig {
    private String bucketName;
    private String baseSubDirectory; // Folder to save file into
}