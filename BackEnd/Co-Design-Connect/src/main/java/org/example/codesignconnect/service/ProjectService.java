package org.example.codesignconnect.service;

import org.example.codesignconnect.model.Project;
import org.example.codesignconnect.model.ProjectMember;

import java.util.List;

public interface ProjectService {
    List<Project> listAllProjects(); // 查看所有项目
    int add(Project project);
    Project getProjectById(Integer id);
    int update(Project project);
    int delete(Integer id);
    List<Project> searchProjects(String name, String category, Integer creatorId, Integer status);
    List<Project> getProjectsByUserId(Integer userId);
    boolean addMemberToProject(Integer projectId, Integer userId);
    boolean exitProject(Integer projectId, Integer userId);
    List<ProjectMember> listMembersByProjectId(Integer projectId);
    Integer createProject(Project project, Integer creatorUserId);
    boolean deleteProject(Integer projectId, Integer userId);
}
