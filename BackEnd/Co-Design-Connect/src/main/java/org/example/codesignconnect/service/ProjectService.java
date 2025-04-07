package org.example.codesignconnect.service;

import org.example.codesignconnect.model.Project;

import java.util.List;

public interface ProjectService {
    List<Project> listAllProjects(); // 查看所有项目
    int add(Project project);
}
