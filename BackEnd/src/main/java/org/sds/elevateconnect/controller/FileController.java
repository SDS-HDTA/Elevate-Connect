package org.sds.elevateconnect.controller;

import lombok.extern.slf4j.Slf4j;
import org.sds.elevateconnect.config.security.RequirePermission;
import org.sds.elevateconnect.dto.FileUploadRequest;
import org.sds.elevateconnect.model.auth.Permission;
import org.sds.elevateconnect.model.Result;
import org.sds.elevateconnect.service.interfaces.IFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RestController
public class FileController {
    @Autowired
    private IFileService fileService;

    @RequirePermission(Permission.ACCESS_RESOURCES_PAGE)
    @GetMapping("projects/files/{id}")
    public Result getFile(@PathVariable Integer id) {
        return Result.success(fileService.getFileById(id));
    }

    @RequirePermission(Permission.ACCESS_RESOURCES_PAGE)
    @GetMapping("/projects/{projectId}/files")
    public Result getProjectFileList(@RequestParam Integer iterationId) {
        log.info("Get project file list");
        return Result.success(fileService.getFilesInFolder(iterationId));
    }

    @RequirePermission(Permission.UPLOAD_FILE)
    @PostMapping("/projects/files")
    public Result createFile(@ModelAttribute FileUploadRequest fileUploadRequest, @RequestParam("file") MultipartFile multipartFile) {
        return Result.success(fileService.addFile(fileUploadRequest, multipartFile));
    }

    // TODO: Restore whiteboard (Miro) functionality
    // Hiding this endpoint for now
//    @RequirePermission(Permission.UPLOAD_FILE)
//    @PostMapping("/projects/files/whiteboard")
//    public Result createWhiteboardFile(@RequestBody File file) {
//        return Result.success(fileService.addFile(file));
//    }

    @RequirePermission(Permission.DELETE_FILE)
    @DeleteMapping("/projects/files/{fileId}")
    public Result deleteFile(@PathVariable Integer fileId) {
        log.info("Delete file");
        fileService.deleteFileById(fileId);
        return Result.success();
    }
}
