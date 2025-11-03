package org.sds.elevateconnect.service.interfaces;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface IGcsService {
    String uploadFile(MultipartFile file, String fileName) throws IOException;
    void deleteFile(String fileName);
    void emptyBucket();
    boolean doesFileNameAlreadyExistInBucket(String fileName);
}
