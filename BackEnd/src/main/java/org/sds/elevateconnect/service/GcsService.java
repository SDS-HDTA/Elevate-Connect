package org.sds.elevateconnect.service;

import com.google.cloud.storage.Blob;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import org.sds.elevateconnect.config.GcsBucketConfig;
import org.sds.elevateconnect.service.interfaces.IGcsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

// Service to handle Google Cloud Storage (GCS) interaction
@Service
public class GcsService implements IGcsService {
    @Autowired
    private Storage bucket;
    @Autowired
    private GcsBucketConfig bucketConfig;

    public String uploadFile(MultipartFile file) throws IOException {
        // File name will be something like "file-uploads/image.png"
        String fileName = getDirectory(file.getOriginalFilename());

        Blob blob = bucket.create(
                BlobInfo.newBuilder(bucketConfig.getBucketName(), fileName).build(),
                file.getBytes()
        );

        // Return URL to file in bucket
        return blob.getMediaLink();
    }

    public void deleteFile(String fileName) {
        // Fetch blob information about the file
        BlobId blobId = BlobId.of(bucketConfig.getBucketName(), getDirectory(fileName));
        bucket.delete(blobId);
    }

    private String getDirectory(String fileName) {
        return bucketConfig.getBaseSubDirectory() + "/" + fileName;
    }
}
