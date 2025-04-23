package org.example.codesignconnect.controller;

import org.example.codesignconnect.model.Project;
import org.example.codesignconnect.model.ProjectMember;
import org.example.codesignconnect.service.ProjectService;
import org.example.codesignconnect.model.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/projects")
public class ProjectController {

    @Autowired
    private ProjectService projectService;

    @GetMapping("/all")
    public Result getAllProjects(Integer page, Integer size) {
        return Result.success(projectService.listAllProjects(page, size));
    }

    @PostMapping
    public Result addProject(@RequestBody Project project) {
        int rows = projectService.add(project);
        return rows > 0 ? Result.success() : Result.error("fail to add project");
    }

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

    @DeleteMapping("/{id}")
    public Result deleteProject(@PathVariable Integer id) {
        int rows = projectService.delete(id);
        return rows > 0 ? Result.success() : Result.error("fail to delete project");
    }

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
    public List<Project> getMyProjects(@RequestParam("userId") Integer userId) {
        return projectService.getProjectsByUserId(userId);
    }

    @PostMapping("/{projectId}/add-member")
    public String addMemberToProject(@PathVariable("projectId") Integer projectId,
                                     @RequestParam("userId") Integer userId) {
        boolean success = projectService.addMemberToProject(projectId, userId);
        if (success) {
            return "Member added successfully.";
        } else {
            return "User is already a member of the project.";
        }
    }


    @PostMapping("/{projectId}/exit")
    public String exitProject(@PathVariable("projectId") Integer projectId,
                              @RequestParam("userId") Integer userId) {
        boolean success = projectService.exitProject(projectId, userId);
        if (success) {
            return "Exited the project successfully.";
        } else {
            return "Failed to exit the project. User might not be a member.";
        }
    }

    @GetMapping("/{projectId}/members")
    public List<ProjectMember> listMembersByProjectId(@PathVariable("projectId") Integer projectId) {
        return projectService.listMembersByProjectId(projectId);
    }

    @PostMapping("/create")
    public Integer createProject(@RequestBody Project project,
                                 @RequestParam("creatorUserId") Integer creatorUserId) {
        return projectService.createProject(project, creatorUserId);
    }

    @DeleteMapping("/{projectId}")
    public String deleteProject(@PathVariable("projectId") Integer projectId,
                                @RequestParam("userId") Integer userId) {
        boolean success = projectService.deleteProject(projectId, userId);
        if (success) {
            return "Project deleted successfully.";
        } else {
            return "Failed to delete project.";
        }
    }
}
