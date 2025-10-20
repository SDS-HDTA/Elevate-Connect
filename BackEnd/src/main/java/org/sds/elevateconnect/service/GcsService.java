package org.sds.elevateconnect.service;

import com.google.api.gax.paging.Page;
import com.google.cloud.storage.*;
import org.sds.elevateconnect.config.GcsBucketConfig;
import org.sds.elevateconnect.service.interfaces.IGcsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Objects;

// Service to handle Google Cloud Storage (GCS) interaction
@Service
public class GcsService implements IGcsService {
    @Autowired
    private Storage bucketStorage;
    @Autowired
    private GcsBucketConfig bucketConfig;

    public String uploadFile(MultipartFile file) throws IOException {
        // File name will be something like "file-uploads/image.png"
        String fileName = getDirectory(file.getOriginalFilename());

        Blob blob = bucketStorage.create(
                BlobInfo.newBuilder(bucketConfig.getBucketName(), fileName).build(),
                file.getBytes()
        );

        // Return URL to file in bucket
        return blob.getMediaLink();
    }

    public void deleteFile(String fileName) {
        // Fetch blob information about the file
        BlobId blobId = BlobId.of(bucketConfig.getBucketName(), getDirectory(fileName));
        bucketStorage.delete(blobId);
    }

    public void emptyBucket() {
        Bucket bucket = bucketStorage.get(bucketConfig.getBucketName());
        Iterable<Blob> allBlobs = bucket.list().iterateAll();

        for (Blob blob : allBlobs) {
            boolean isProjectFile = blob.getName().startsWith(bucketConfig.getBaseSubDirectory());
            boolean isDefaultProjectFile = blob.getName().contains("ProjectManagement.jpg");

            if (isProjectFile && !isDefaultProjectFile) {
                blob.delete();
            }
        }
    }

    private String getDirectory(String fileName) {
        return bucketConfig.getBaseSubDirectory() + "/" + fileName;
    }
}
