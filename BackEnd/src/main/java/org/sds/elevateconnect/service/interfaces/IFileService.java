package org.sds.elevateconnect.service.interfaces;

import org.sds.elevateconnect.model.project.File;

import java.util.List;

public interface IFileService {
    File getFileById(Integer id);
    List<File> getAllFiles();
    File addFile(File file);
    void updateFile(File file);
    void deleteFileById(Integer id);
    List<File> getFilesInFolder(Integer iterationId);
}
