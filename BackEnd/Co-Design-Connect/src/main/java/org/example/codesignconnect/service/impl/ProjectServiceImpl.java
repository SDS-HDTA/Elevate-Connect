package org.example.codesignconnect.service.impl;

import org.example.codesignconnect.mapper.ProjectMapper;
import org.example.codesignconnect.mapper.ProjectMemberMapper;
import org.example.codesignconnect.model.Project;
import org.example.codesignconnect.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ProjectServiceImpl implements ProjectService {

    @Autowired
    private ProjectMapper projectMapper;

    @Autowired
    private ProjectMemberMapper projectMemberMapper;

    @Override
    public List<Project> listAllProjects() {
        return projectMapper.getAllProjects();
    }

    @Override
    public int add(Project project) {
        return projectMapper.insertProject(project);
    }

    @Override
    public Project getProjectById(Integer id) {
        return projectMapper.getProjectById(id);
    }

    @Override
    public int update(Project project) {
        return projectMapper.updateProject(project);
    }

    @Override
    public int delete(Integer id) {
        return projectMapper.deleteProject(id);
    }

    @Override
    public List<Project> searchProjects(String name, String category, Integer creatorId, Integer status) {
        return projectMapper.searchProjects(name, category, creatorId, status);
    }

    @Override
    public List<Project> getProjectsByUserId(Integer userId) {
        // Step 1: Get all project IDs the user has joined
        List<Integer> projectIds = projectMemberMapper.findProjectIdsByUserId(userId);

        // Step 2: If no projects found, return an empty list
        if (projectIds == null || projectIds.isEmpty()) {
            return new ArrayList<>();
        }

        // Step 3: Retrieve project details by project IDs
        return projectMapper.findProjectsByIds(projectIds);


    }
}
