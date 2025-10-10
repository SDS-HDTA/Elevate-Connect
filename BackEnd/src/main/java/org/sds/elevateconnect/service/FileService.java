package org.sds.elevateconnect.service;

import org.sds.elevateconnect.mapper.FileMapper;
import org.sds.elevateconnect.model.project.File;
import org.sds.elevateconnect.service.interfaces.IFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FileService implements IFileService {
    @Autowired
    private FileMapper fileMapper;

    @Override
    public File getFileById(Integer id) {
        return fileMapper.selectFileById(id);
    }

    @Override
    public List<File> getAllFiles() {
        return fileMapper.selectAllFiles();
    }

    @Override
    public File addFile(File file) {
        fileMapper.insertFile(file);
        return file;
    }

    @Override
    public void updateFile(File file) {
        fileMapper.updateFile(file);
    }

    @Override
    public void deleteFileById(Integer id) {
        fileMapper.deleteFileById(id);
    }

    @Override
    public List<File> getFilesInFolder(Integer iterationId) {
        return fileMapper.selectFilesInFolder(iterationId);
    }
}
