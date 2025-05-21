package org.example.codesignconnect.controller;

import org.example.codesignconnect.dto.ProjectDetail;
import org.example.codesignconnect.model.Project;
import org.example.codesignconnect.model.ProjectMember;
import org.example.codesignconnect.model.User;
import org.example.codesignconnect.service.ProjectService;
import org.example.codesignconnect.model.Result;
import org.example.codesignconnect.service.UserService;
import org.example.codesignconnect.utils.AliyunOSSOperator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/projects")
public class ProjectController {

    @Autowired
    private ProjectService projectService;

    @Autowired
    private UserService userService;

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


    @GetMapping("/{projectId}")
    public Result getProjectById(@PathVariable Integer projectId) {
        Project project = projectService.getProjectById(projectId);
        if(project == null) return Result.error("project not found");
        else{
            Integer creatorId = project.getCreatorId();
            User creator = (User)userService.getUserInfo(creatorId).getData();
            ProjectDetail projectDetail = new ProjectDetail(project, creator.getUsername());
            return Result.success(projectDetail);
        }
    }

    @PutMapping("/{id}")
    public Result updateProject(@PathVariable Integer id, @RequestBody Project project) {
        project.setId(id);
        int rows = projectService.update(project);
        return rows > 0 ? Result.success() : Result.error("fail to update project");
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
    public Result getMyProjects(
            @RequestParam("userId") Integer userId,
            @RequestParam(value = "searchType", required = false) Integer searchType,
            @RequestParam(value = "searchValue", required = false) String searchValue
    ) {
        return Result.success(projectService.getProjectsByUserId(userId, searchType, searchValue));
    }


    @PostMapping("/join")
    public Result joinProject(@RequestParam("projectId") Integer projectId,
                              @RequestParam("userId") Integer userId) {
        Project project = projectService.getProjectById(projectId);
        if (project == null) {
            return Result.error("Project not found");
        }
        if (projectService.isUserMemberOfProject(projectId, userId)) {
            return Result.error("User is already a member of the project");
        }
        int memberCount = projectService.getMemberCount(projectId);
        if (memberCount >= 10) { //testValue
            return Result.error("The project has reached the maximum number of members");
        }
        boolean success = projectService.addMemberToProject(projectId, userId);
        return success ? Result.success() : Result.error("Failed to join the project");
    }

    @GetMapping("/{projectId}/members")
    public Result listMembersByProjectId(@PathVariable("projectId") Integer projectId) {
        return Result.success(projectService.listMembersByProjectId(projectId));
    }

    @DeleteMapping("/{projectId}/members/{userId}")
    public Result removeMember(
            @PathVariable("projectId") Integer projectId,
            @PathVariable("userId") Integer userId) {
        boolean success = projectService.removeMemberFromProject(projectId, userId);
        return success ? Result.success("Member removed successfully") : Result.error("Failed to remove member");
    }

    @PostMapping("/leave")
    public Result leaveProject(@RequestParam("projectId") Integer projectId,
                               @RequestParam("userId") Integer userId) {
        boolean success = projectService.exitProject(projectId, userId);
        return success ? Result.success("Successfully left the project") : Result.error("Failed to leave project");
    }

    @DeleteMapping("/{projectId}/dismiss")
    public Result dismissProject(@PathVariable Integer projectId) {
        boolean success = projectService.dismissProject(projectId);
        if (success) {
            return Result.success("Project has been successfully dismissed");
        } else {
            return Result.error("Failed to dismiss project");
        }
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

    @GetMapping("/searchByName")
    public Result searchProjectByName(@RequestParam("name") String name) {
        List<Project> list = projectService.searchProjectByName(name);
        return Result.success(list);
    }
}
