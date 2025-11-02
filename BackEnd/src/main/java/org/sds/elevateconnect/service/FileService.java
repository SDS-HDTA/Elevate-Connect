package org.sds.elevateconnect.service;

import org.sds.elevateconnect.dto.FileUploadRequest;
import org.sds.elevateconnect.exceptions.FileException;
import org.sds.elevateconnect.mapper.FileMapper;
import org.sds.elevateconnect.mapper.IterationMapper;
import org.sds.elevateconnect.model.project.File;
import org.sds.elevateconnect.model.project.FileType;
import org.sds.elevateconnect.model.project.Project;
import org.sds.elevateconnect.service.interfaces.IFileService;
import org.sds.elevateconnect.service.interfaces.IGcsService;
import org.sds.elevateconnect.utils.Validator;
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
        FileType fileType = FileType.fromInt(fileUploadRequest.getType());

        if (!Validator.isValidFile(bucketFileObject, fileType)) {
            throw new FileException("File extension is invalid.");
        }

        try {
            // File object to insert into database
            File file = File.builder()
                    .iterationId(fileUploadRequest.getIterationId())
                    .creatorId(fileUploadRequest.getCreatorId())
                    .name(bucketFileObject.getOriginalFilename()) // Infer from image upload
                    .type(fileType)
                    .build();

            file.setName(createValidFileName(file.getName()));

            // bucketFileObject represents the real file data
            String fileUrl = gcsService.uploadFile(bucketFileObject, file.getName());
            file.setSource(fileUrl);

            // dbFileObject.id is set after insert
            fileMapper.insertFile(file);

            return fileMapper.selectFileById(file.getId());
        } catch (IOException e) {
            throw new FileException("Failed to upload file to bucket.");
        }
    }

    @Override
    public String createValidFileName(String currentFileName) {
        if (gcsService.doesFileNameAlreadyExistInBucket(currentFileName)) {
            int i = 1;
            String newFileName = createNewFileName(currentFileName, i);

            while (gcsService.doesFileNameAlreadyExistInBucket(newFileName)) {
                i++;
                newFileName = createNewFileName(currentFileName, i);
            }

            return newFileName;
        }

        return currentFileName;
    }

    private String createNewFileName(String fileName, int i) {
        int lastDotIndex = fileName.lastIndexOf(".");

        if (lastDotIndex == -1) {
            return fileName + i;
        }

        String[] splitFileName = fileName.split("\\.");
        return splitFileName[0] + i + "." + splitFileName[1];
    }

    @Override
    public File addProjectImage(Project project, MultipartFile projectImage) {
        return addFile(
                new FileUploadRequest(
                    null, // Project images are not assigned to an iteration of a project
                        project.getCreatorId(),
                        FileType.IMAGE.getIntValue()
                ),
                projectImage
        );
    }

    @Override
    public void deleteFileById(Integer id) {
        File file = getFileById(id);
        gcsService.deleteFile(file.getName());
        fileMapper.deleteFileById(id);
    }

    @Override
    public List<File> getFilesInFolder(Integer iterationId) {
        return fileMapper.selectFilesInFolder(iterationId);
    }
}
