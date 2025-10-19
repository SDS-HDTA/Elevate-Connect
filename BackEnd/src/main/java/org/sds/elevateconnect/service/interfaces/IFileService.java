package org.sds.elevateconnect.service.interfaces;

import org.sds.elevateconnect.dto.FileUploadRequest;
import org.sds.elevateconnect.model.project.File;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface IFileService {
    File getFileById(Integer id);
    File addFile(FileUploadRequest file, MultipartFile bucketFileObject);
    void deleteFileById(Integer id);
    List<File> getFilesInFolder(Integer iterationId);
}
