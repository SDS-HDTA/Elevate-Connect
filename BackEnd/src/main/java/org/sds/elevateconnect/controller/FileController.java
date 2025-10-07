package org.sds.elevateconnect.controller;

import lombok.extern.slf4j.Slf4j;
import org.sds.elevateconnect.config.security.RequirePermission;
import org.sds.elevateconnect.model.auth.Permission;
import org.sds.elevateconnect.model.project.File;
import org.sds.elevateconnect.model.Result;
import org.sds.elevateconnect.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.sds.elevateconnect.utils.AliyunOSSOperator;

@Slf4j
@RestController
public class FileController {
    @Autowired
    private FileService fileService;

    @Autowired
    private AliyunOSSOperator aliyunOSSOperator;

    @RequirePermission(Permission.ACCESS_RESOURCES_PAGE)
    @GetMapping("/{id}")
    public Result getFile(@PathVariable Integer id) {
        return Result.success(fileService.getFileById(id));
    }

    @RequirePermission(Permission.ACCESS_RESOURCES_PAGE)
    @GetMapping("/projects/{projectId}/files")
    public Result getProjectFileList(@PathVariable Integer projectId, @RequestParam Short projectStatus, @RequestParam Integer iterationId) {
        log.info("Get project file list");
        return Result.success(fileService.getFilesInFolder(projectId, projectStatus, iterationId));
    }

    @RequirePermission(Permission.UPLOAD_FILE)
    @PostMapping("/projects/files/documents")
    public Result createDocumentFile(@ModelAttribute File file, @RequestParam("multipartFile") MultipartFile multipartFile) throws Exception{
        //String url = aliyunOSSOperator.upload(multipartFile.getBytes(), Objects.requireNonNull(multipartFile.getOriginalFilename()));
        file.setSource("url");
        return Result.success(fileService.addFile(file));
    }

    @RequirePermission(Permission.UPLOAD_FILE)
    @PostMapping("/projects/files/whiteboard")
    public Result createWhiteboardFile(@RequestBody File file) {
        return Result.success(fileService.addFile(file));
    }

    @RequirePermission(Permission.UPLOAD_FILE)
    @PostMapping("/projects/files/pictures")
    public Result createImageFile(@ModelAttribute File file, @RequestParam("multipartFile") MultipartFile multipartFile) throws Exception{
        //String url = aliyunOSSOperator.upload(multipartFile.getBytes(), Objects.requireNonNull(multipartFile.getOriginalFilename()));
        file.setSource("url");
        return Result.success(fileService.addFile(file));
    }

    @RequirePermission(Permission.UPLOAD_FILE)
    @PostMapping("/projects/files/videos")
    public Result createVideoFile(@ModelAttribute File file, @RequestParam("multipartFile") MultipartFile multipartFile) throws Exception{
        //String url = aliyunOSSOperator.upload(multipartFile.getBytes(), Objects.requireNonNull(multipartFile.getOriginalFilename()));
        file.setSource("url");
        return Result.success(fileService.addFile(file));
    }

    @PutMapping("/projects/{projectId}/files")
    public Result updateFile(@RequestBody File file) {
        fileService.updateFile(file);
        return Result.success();
    }

    @RequirePermission(Permission.DELETE_FILE)
    @DeleteMapping("/projects/files/{fileId}")
    public Result deleteFile(@PathVariable Integer fileId) {
        log.info("Delete file");
        fileService.deleteFileById(fileId);
        return Result.success();
    }
}
