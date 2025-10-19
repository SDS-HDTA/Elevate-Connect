package org.sds.elevateconnect.service;

import org.sds.elevateconnect.dto.FileUploadRequest;
import org.sds.elevateconnect.dto.GcsFileRequest;
import org.sds.elevateconnect.exceptions.FileException;
import org.sds.elevateconnect.mapper.FileMapper;
import org.sds.elevateconnect.mapper.IterationMapper;
import org.sds.elevateconnect.model.project.File;
import org.sds.elevateconnect.model.project.FileType;
import org.sds.elevateconnect.service.interfaces.IFileService;
import org.sds.elevateconnect.service.interfaces.IGcsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public class FileService implements IFileService {
    @Autowired
    private IGcsService gcsService;
    @Autowired
    private FileMapper fileMapper;
    @Autowired
    private IterationMapper iterationMapper;

    @Override
    public File getFileById(Integer id) {
        return fileMapper.selectFileById(id);
    }

    @Override
    public File addFile(FileUploadRequest fileUploadRequest, MultipartFile bucketFileObject) {
        try {
            // File object to insert into database
            File file = File.builder()
                    .iterationId(fileUploadRequest.iterationId())
                    .creatorId(fileUploadRequest.creatorId())
                    .name(fileUploadRequest.name())
                    .type(FileType.fromInt(fileUploadRequest.type()))
                    .build();

            // bucketFileObject represents the real file data
            String fileUrl = gcsService.uploadFile(bucketFileObject);
            file.setSource(fileUrl);

            // dbFileObject.id is set after insert
            fileMapper.insertFile(file);

            return fileMapper.selectFileById(file.getId());
        } catch (IOException e) {
            throw new FileException("Failed to upload file to bucket.");
        }
    }

    @Override
    public void deleteFileById(Integer id) {
        gcsService.deleteFile(fileMapper.selectFileById(id).getName());
        fileMapper.deleteFileById(id);
    }

    @Override
    public List<File> getFilesInFolder(Integer iterationId) {
        return fileMapper.selectFilesInFolder(iterationId);
    }
}
