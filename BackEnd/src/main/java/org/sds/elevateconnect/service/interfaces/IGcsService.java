package org.sds.elevateconnect.service.interfaces;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface IGcsService {
    String uploadFile(MultipartFile file) throws IOException;
    void deleteFile(String fileName);
}
