package org.sds.elevateconnect.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.sds.elevateconnect.model.project.File;

import java.util.List;

@Mapper
public interface FileMapper {
    File selectFileById(Integer id);
    List<File> selectAllFiles();
    void insertFile(File file);
    void updateFile(File file);
    void deleteFileById(Integer id);
    List<File> selectFilesInFolder(Integer iterationId);
}
