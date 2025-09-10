package org.sds.elevateconnect.service.impl;

import org.sds.elevateconnect.dto.FileDetail;
import org.sds.elevateconnect.mapper.FileMapper;
import org.sds.elevateconnect.mapper.UserMapper;
import org.sds.elevateconnect.model.File;
import org.sds.elevateconnect.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class FileServiceImpl implements FileService {
    @Autowired
    private FileMapper fileMapper;

    @Autowired
    private UserMapper userMapper;

    @Override
    public File getFileById(Integer id) {
        return fileMapper.selectFileById(id);
    }

    @Override
    public List<File> getAllFiles() {
        return fileMapper.selectAllFiles();
    }

    @Override
    public FileDetail addFile(File file) {
        fileMapper.insertFile(file);
        return new FileDetail(file, userMapper.getFullNameById(file.getCreatorId()));
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
    public List<FileDetail> getFilesInFolder(Integer projectId, Short projectStatus, Integer iterationId) {
        List<File> fileList = fileMapper.selectFilesInFolder(projectId, projectStatus, iterationId);
        List<FileDetail> fileDetailList = new ArrayList<>();
        for (File file : fileList) {
            fileDetailList.add(new FileDetail(file, userMapper.getFullNameById(file.getCreatorId())));
        }
        return fileDetailList;
    }
}
