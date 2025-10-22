package org.sds.elevateconnect.service.interfaces;

import org.sds.elevateconnect.dto.FileUploadRequest;
import org.sds.elevateconnect.model.project.File;
import org.sds.elevateconnect.model.project.Project;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface IFileService {
    File getFileById(Integer id);
    File addFile(FileUploadRequest file, MultipartFile bucketFileObject);
    File addProjectImage(Project project, MultipartFile projectImage);
    void deleteFileById(Integer id);
    List<File> getFilesInFolder(Integer iterationId);
}
