package org.example.codesignconnect.service;

import org.example.codesignconnect.model.Project;

import java.util.List;

public interface ProjectService {
    List<Project> listAllProjects(); // 查看所有项目
    int add(Project project);
    Project getProjectById(Integer id);
    int update(Project project);
    int delete(Integer id);
    List<Project> searchProjects(String name, String category, Integer creatorId, Integer status);
}
