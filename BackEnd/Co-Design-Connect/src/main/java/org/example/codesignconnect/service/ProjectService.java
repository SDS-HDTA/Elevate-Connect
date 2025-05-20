package org.example.codesignconnect.service;

import org.example.codesignconnect.model.PageResult;
import org.example.codesignconnect.model.Project;
import org.example.codesignconnect.model.ProjectMember;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ProjectService {
    PageResult<Project> listAllProjects(Integer page, Integer size, Integer searchType, String searchValue);
    int add(Project project);
    Project getProjectById(Integer id);
    List<Project> getProjectsByUserId(Integer userId, Integer searchType, String searchValue);
    int update(Project project);
    int delete(Integer id);
    List<Project> searchProjects(String name, String category, Integer creatorId, Integer status);
    List<Project> getProjectsByUserId(Integer userId);
    boolean addMemberToProject(Integer projectId, Integer userId);
    boolean exitProject(Integer projectId, Integer userId);
    List<ProjectMember> listMembersByProjectId(Integer projectId);
    Project createProject(Project project);
    boolean deleteProject(Integer projectId, Integer userId);
    boolean isUserMemberOfProject(Integer projectId, Integer userId);
    int getMemberCount(Integer projectId);
    List<Project> searchProjectByName(String name);
    boolean removeMemberFromProject(Integer projectId, Integer userId);
    boolean dismissProject(Integer projectId);
}
