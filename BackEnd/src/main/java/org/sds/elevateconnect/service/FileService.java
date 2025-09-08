package org.sds.elevateconnect.service;

import org.sds.elevateconnect.dto.FileDetail;
import org.sds.elevateconnect.model.File;

import java.util.List;

public interface FileService {
    File getFileById(Integer id);
    List<File> getAllFiles();
    FileDetail addFile(File file);
    void updateFile(File file);
    void deleteFileById(Integer id);
    List<FileDetail> getFilesInFolder(Integer projectId, Short projectStatus, Integer iterationId);
}
