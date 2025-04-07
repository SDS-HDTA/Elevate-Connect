package org.example.codesignconnect.controller;

import org.example.codesignconnect.model.Project;
import org.example.codesignconnect.service.ProjectService;
import org.example.codesignconnect.model.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/projects")
public class ProjectController {

    @Autowired
    private ProjectService projectService;

    @GetMapping
    public Result getAllProjects() {
        return Result.success(projectService.listAllProjects());
    }

    @PostMapping
    public Result addProject(String name, String description, String category, String region,
                             Integer creatorId, Integer status, String imageUrl,
                             Integer channelId, String tags) {

        Project project = new Project();
        project.setName(name);
        project.setDescription(description);
        project.setCategory(category);
        project.setRegion(region);
        project.setCreatorId(creatorId);
        project.setStatus(status);
        project.setImageUrl(imageUrl);
        project.setChannelId(channelId);
        project.setTags(tags);

        int rows = projectService.add(project);
        return rows > 0 ? Result.success() : Result.error("添加项目失败");
    }
}
