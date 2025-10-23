package org.sds.elevateconnect.controller;

import org.sds.elevateconnect.config.security.RequirePermission;
import org.sds.elevateconnect.dto.CreateProjectRequest;
import org.sds.elevateconnect.dto.UpdateProjectRequest;
import org.sds.elevateconnect.dto.UserDetail;
import org.sds.elevateconnect.model.auth.Permission;
import org.sds.elevateconnect.model.auth.User;
import org.sds.elevateconnect.service.ProjectService;
import org.sds.elevateconnect.model.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

@RestController
@RequestMapping("/projects")
public class ProjectController {
    @Autowired
    private ProjectService projectService;

    @RequirePermission(Permission.CREATE_PROJECT)
    @PostMapping("/create")
    public Result createProject(@AuthenticationPrincipal User user, @ModelAttribute CreateProjectRequest createProjectRequest, @RequestParam("projectImage") MultipartFile projectImage) {
        createProjectRequest.setCreatorId(user.getId());
        projectService.createProject(createProjectRequest, projectImage);
        return Result.success();
    }

    @RequirePermission(Permission.ACCESS_DISCOVER_PAGE)
    @GetMapping("/all")
    public Result getAllProjectsWithPaginationAndSearching(
            @AuthenticationPrincipal User user,
            @RequestParam Integer page,
            @RequestParam Integer size,
            @RequestParam(required = false) Integer searchType,
            @RequestParam(required = false) String searchValue
    ) {
        return Result.success(projectService.getPaginatedListOfAllProjects(user.getId(), page, size, searchType, searchValue));
    }

    @GetMapping("/{projectId}")
    public Result getProjectById(@PathVariable Integer projectId) {
        return Result.success(projectService.getProjectById(projectId));
    }

    @RequirePermission(Permission.ACCESS_MY_PROJECTS_PAGE)
    @GetMapping("/my")
    public Result getMyProjects(
            @AuthenticationPrincipal User user,
            @RequestParam(value = "searchType", required = false) Integer searchType,
            @RequestParam(value = "searchValue", required = false) String searchValue
    ) {
        return Result.success(projectService.searchMyProjects(user.getId(), searchType, searchValue));
    }

    @RequirePermission(Permission.ACCESS_PARTICIPANTS_PAGE)
    @GetMapping("/{projectId}/members")
    public Result getMembersByProjectId(@PathVariable("projectId") Integer projectId) {
        return Result.success(projectService.listMembersByProjectId(projectId));
    }

    @GetMapping("/searchByName")
    public Result searchProjectByName(@RequestParam("name") String name) {
        return Result.success(projectService.searchProjectByName(name));
    }

    @RequirePermission(Permission.ACCESS_ACTIVITIES_PAGE)
    @GetMapping("/{projectId}/stage")
    public Result getProjectStage(@PathVariable Integer projectId) {
        return Result.success(projectService.getProjectStage(projectId));
    }

    @RequirePermission(Permission.MODIFY_CURRENT_PROJECT_STAGE)
    @PutMapping("/{projectId}/stage")
    public Result updateProjectStage(@PathVariable Integer projectId, @RequestParam("projectStage") Integer projectStage) {
        projectService.updateProjectStage(projectId, projectStage);
        return Result.success();
    }

    @RequirePermission(Permission.EDIT_PROJECT)
    @PutMapping("/{id}")
    public Result updateProject(@PathVariable Integer id, 
                                @ModelAttribute UpdateProjectRequest updateProjectRequest, 
                                @RequestParam(value = "projectImage", required = false) MultipartFile projectImage) {
        projectService.updateProject(id, updateProjectRequest, projectImage);
        return Result.success();
    }

    @RequirePermission(Permission.EDIT_PROJECT)
    @DeleteMapping("/{projectId}/members/{userId}")
    public Result removeMember(@PathVariable("projectId") Integer projectId,
                               @PathVariable("userId") Integer userId) {
        projectService.removeMemberFromProject(projectId, userId);
        List<UserDetail> members = projectService.listMembersByProjectId(projectId);
        return new Result(1, "Member removed successfully", members);
    }

    @RequirePermission(Permission.DELETE_PROJECT)
    @DeleteMapping("/{projectId}")
    public Result deleteProject(@PathVariable("projectId") Integer projectId) {
        projectService.deleteProject(projectId);
        return Result.success();
    }
}
