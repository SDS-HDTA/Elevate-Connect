package org.example.codesignconnect.service.impl;

import org.example.codesignconnect.mapper.ProjectMapper;
import org.example.codesignconnect.model.Project;
import org.example.codesignconnect.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProjectServiceImpl implements ProjectService {

    @Autowired
    private ProjectMapper projectMapper;

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

}
