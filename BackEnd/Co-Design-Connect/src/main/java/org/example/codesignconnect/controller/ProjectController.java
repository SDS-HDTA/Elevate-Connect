package org.example.codesignconnect.controller;

import org.example.codesignconnect.model.Project;
import org.example.codesignconnect.model.ProjectMember;
import org.example.codesignconnect.service.ProjectService;
import org.example.codesignconnect.model.Result;
import org.example.codesignconnect.utils.AliyunOSSOperator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/projects")
public class ProjectController {

    @Autowired
    private ProjectService projectService;

    @Autowired
    private AliyunOSSOperator aliyunOSSOperator;

    @GetMapping("/all")
    public Result getAllProjects(
            @RequestParam Integer page,
            @RequestParam Integer size,
            @RequestParam(required = false) Integer searchType,
            @RequestParam(required = false) String searchValue
    ) {
        return Result.success(projectService.listAllProjects(page, size, searchType, searchValue));
    }


//    @PostMapping
//    public Result addProject(@RequestBody Project project) {
//        int rows = projectService.add(project);
//        return rows > 0 ? Result.success() : Result.error("fail to add project");
//    }

    @GetMapping("/project/{id}")
    public Result getProjectById(@PathVariable Integer id) {
        Project project = projectService.getProjectById(id);
        return project != null ? Result.success(project) : Result.error("project not found");
    }

    @PutMapping("/{id}")
    public Result updateProject(@PathVariable Integer id, @RequestBody Project project) {
        project.setId(id);
        int rows = projectService.update(project);
        return rows > 0 ? Result.success() : Result.error("fail to update project");
    }

//    @DeleteMapping("/{id}")
//    public Result deleteProject(@PathVariable Integer id) {
//        int rows = projectService.delete(id);
//        return rows > 0 ? Result.success() : Result.error("fail to delete project");
//    }

    @GetMapping("/search")
    public Result searchProjects(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) Integer creatorId,
            @RequestParam(required = false) Integer status
    ) {
        List<Project> projects = projectService.searchProjects(name, category, creatorId, status);
        return Result.success(projects);
    }

    @GetMapping("/my")
    public Result getMyProjects(@RequestParam("userId") Integer userId) {
        return Result.success(projectService.getProjectsByUserId(userId));
    }

    @PostMapping("/{projectId}/add-member")
    public Result addMemberToProject(@PathVariable("projectId") Integer projectId,
                                     @RequestParam("userId") Integer userId) {
        boolean success = projectService.addMemberToProject(projectId, userId);
        if (success) {
            return Result.success();
        } else {
            return Result.error("User is already a member of the project.");
        }
    }


    @PostMapping("/{projectId}/exit")
    public Result exitProject(@PathVariable("projectId") Integer projectId,
                              @RequestParam("userId") Integer userId) {
        boolean success = projectService.exitProject(projectId, userId);
        if (success) {
            return Result.success();
        } else {
            return Result.error("Failed to exit the project. User might not be a member.");
        }
    }

    @GetMapping("/{projectId}/members")
    public Result listMembersByProjectId(@PathVariable("projectId") Integer projectId) {
        return Result.success(projectService.listMembersByProjectId(projectId));
    }

    @PostMapping("/create")
    public Result createProject(@ModelAttribute Project project, @RequestParam("image") MultipartFile file) throws Exception {
        String url = aliyunOSSOperator.upload(file.getBytes(), Objects.requireNonNull(file.getOriginalFilename()));
        project.setImageUrl(url);
        return Result.success(projectService.createProject(project));
    }

    @DeleteMapping("/{projectId}")
    public Result deleteProject(@PathVariable("projectId") Integer projectId,
                                @RequestParam("userId") Integer userId) {
        boolean success = projectService.deleteProject(projectId, userId);
        if (success) {
            return Result.success();
        } else {
            return Result.error("Failed to delete project.");
        }
    }
}
