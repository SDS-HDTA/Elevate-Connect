package org.sds.elevateconnect.controller;

import org.sds.elevateconnect.dto.CreateProjectRequest;
import org.sds.elevateconnect.dto.UserDetail;
import org.sds.elevateconnect.exceptions.ProjectException;
import org.sds.elevateconnect.model.project.Project;
import org.sds.elevateconnect.model.project.ProjectStage;
import org.sds.elevateconnect.service.ProjectService;
import org.sds.elevateconnect.model.Result;
import org.sds.elevateconnect.service.UserService;
import org.sds.elevateconnect.utils.AliyunOSSOperator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

@RestController
@RequestMapping("/projects")
public class ProjectController {
    @Autowired
    private ProjectService projectService;
    @Autowired
    private UserService userService;
    @Autowired
    private AliyunOSSOperator aliyunOSSOperator;

    @PostMapping("/create")
    public Result createProject(@RequestBody CreateProjectRequest createProjectRequest, @RequestParam("image") MultipartFile file) throws Exception {
        return Result.success(projectService.createProject(createProjectRequest));
    }

    @PostMapping("/join")
    public Result joinProject(@RequestParam("projectId") Integer projectId, @RequestParam("userId") Integer userId) {
        try {
            projectService.joinProject(projectId, userId);
            return Result.success();
        } catch (ProjectException e) {
            return Result.error(e.getMessage());
        }
    }

    @GetMapping("/all")
    public Result getAllProjectsWithPaginationAndSearching(
            @RequestParam Integer page,
            @RequestParam Integer size,
            @RequestParam(required = false) Integer searchType,
            @RequestParam(required = false) String searchValue
    ) {
        try {
            return Result.success(projectService.getPaginatedListOfAllProjects(page, size, searchType, searchValue));
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @GetMapping("/{projectId}")
    public Result getProjectById(@PathVariable Integer projectId) {
        try {
            return Result.success(projectService.getProjectById(projectId));
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @GetMapping("/my")
    public Result getMyProjects(
            @RequestParam("userId") Integer userId,
            @RequestParam(value = "searchType", required = false) Integer searchType,
            @RequestParam(value = "searchValue", required = false) String searchValue
    ) {
        return Result.success(projectService.searchMyProjects(userId, searchType, searchValue));
    }

    @GetMapping("/{projectId}/members")
    public Result getMembersByProjectId(@PathVariable("projectId") Integer projectId) {
        try {
            return Result.success(projectService.listMembersByProjectId(projectId));
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @GetMapping("/searchByName")
    public Result searchProjectByName(@RequestParam("name") String name) {
        List<Project> list = projectService.searchProjectByName(name);
        return Result.success(list);
    }

    @GetMapping("/{projectId}/status")
    public Result getProjectStatus(@PathVariable Integer projectId) {
        ProjectStage projectStage = projectService.getProjectStage(projectId);

        return Result.success(projectStage.getIntValue());
    }

    @PutMapping("/{projectId}/status")
    public Result updateProjectStatus(@PathVariable Integer projectId, Integer projectStage) {
        projectService.updateProjectStage(projectId, projectStage);
        return Result.success();
    }

    @PutMapping("/{id}")
    public Result updateProject(@PathVariable Integer id, @RequestBody Project project) {
        project.setId(id);
        int rows = projectService.update(project);
        return rows > 0 ? Result.success() : Result.error("fail to update project");
    }

    @DeleteMapping("/{projectId}/members/{userId}")
    public Result removeMember(@PathVariable("projectId") Integer projectId,
                               @PathVariable("userId") Integer userId) {
        boolean success = projectService.removeMemberFromProject(projectId, userId);
        if (success) {
            List<UserDetail> members = projectService.listMembersByProjectId(projectId);
            return new Result(1, "Member removed successfully", members);
        } else {
            return Result.error("Failed to remove member");
        }
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
