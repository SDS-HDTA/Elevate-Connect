package org.sds.elevateconnect.controller;

import org.sds.elevateconnect.dto.CreateProjectRequest;
import org.sds.elevateconnect.dto.UserDetail;
import org.sds.elevateconnect.model.auth.User;
import org.sds.elevateconnect.model.project.Project;
import org.sds.elevateconnect.service.ProjectService;
import org.sds.elevateconnect.model.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/projects")
public class ProjectController {
    @Autowired
    private ProjectService projectService;

    @PostMapping("/create")
    public Result createProject(@RequestBody CreateProjectRequest createProjectRequest) {
        projectService.createProject(createProjectRequest);
        return Result.success();
    }

    @PostMapping("/join")
    public Result joinProject(@RequestParam("projectId") Integer projectId, @AuthenticationPrincipal User user) {
        projectService.joinProject(projectId, user.getId());
        return Result.success();
    }

    @GetMapping("/all")
    public Result getAllProjectsWithPaginationAndSearching(
            @RequestParam Integer page,
            @RequestParam Integer size,
            @RequestParam(required = false) Integer searchType,
            @RequestParam(required = false) String searchValue
    ) {
        return Result.success(projectService.getPaginatedListOfAllProjects(page, size, searchType, searchValue));
    }

    @GetMapping("/{projectId}")
    public Result getProjectById(@PathVariable Integer projectId) {
        return Result.success(projectService.getProjectById(projectId));
    }

    @GetMapping("/my")
    public Result getMyProjects(
            @AuthenticationPrincipal User user,
            @RequestParam(value = "searchType", required = false) Integer searchType,
            @RequestParam(value = "searchValue", required = false) String searchValue
    ) {
        return Result.success(projectService.searchMyProjects(user.getId(), searchType, searchValue));
    }

    @GetMapping("/{projectId}/members")
    public Result getMembersByProjectId(@PathVariable("projectId") Integer projectId) {
        return Result.success(projectService.listMembersByProjectId(projectId));
    }

    @GetMapping("/searchByName")
    public Result searchProjectByName(@RequestParam("name") String name) {
        return Result.success(projectService.searchProjectByName(name));
    }

    @GetMapping("/{projectId}/stage")
    public Result getProjectStage(@PathVariable Integer projectId) {
        return Result.success(projectService.getProjectStage(projectId));
    }

    @PutMapping("/{projectId}/stage")
    public Result updateProjectStage(@PathVariable Integer projectId, @RequestParam("projectStage") Integer projectStage) {
        projectService.updateProjectStage(projectId, projectStage);
        return Result.success();
    }

    // TODO: NEED TO CHECK THIS LINES UP WITH UPDATE PROJECT DIALOG ON FRONTEND
    @PutMapping("/{id}")
    public Result updateProject(@PathVariable Integer id, @RequestBody Project project) {
        project.setId(id);
        projectService.update(project);
        return Result.success();
    }

    @DeleteMapping("/{projectId}/members/{userId}")
    public Result removeMember(@PathVariable("projectId") Integer projectId,
                               @PathVariable("userId") Integer userId) {
        projectService.removeMemberFromProject(projectId, userId);
        List<UserDetail> members = projectService.listMembersByProjectId(projectId);
        return new Result(1, "Member removed successfully", members);
    }

    @DeleteMapping("/{projectId}")
    public Result deleteProject(@PathVariable("projectId") Integer projectId) {
        projectService.deleteProject(projectId);
        return Result.success();
    }
}
