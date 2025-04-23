package org.example.codesignconnect.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.example.codesignconnect.mapper.ProjectMapper;
import org.example.codesignconnect.mapper.ProjectMemberMapper;
import org.example.codesignconnect.model.PageResult;
import org.example.codesignconnect.model.Project;
import org.example.codesignconnect.model.ProjectMember;
import org.example.codesignconnect.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class ProjectServiceImpl implements ProjectService {

    @Autowired
    private ProjectMapper projectMapper;

    @Autowired
    private ProjectMemberMapper projectMemberMapper;

    @Override
    public PageResult<Project> listAllProjects(Integer page, Integer size) {
        PageHelper.startPage(page, size);
        List<Project> projectList = projectMapper.getAllProjects();
        Page<Project> p = (Page<Project>) projectList;
        return new PageResult<Project>(p.getTotal(), p.getResult());
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

    @Override
    public boolean addMemberToProject(Integer projectId, Integer userId) {
        // Check if the user is already a member
        ProjectMember existing = projectMemberMapper.findProjectMember(projectId, userId);
        if (existing != null) {
            // Already a member, do not add again
            return false;
        }

        // Create a new ProjectMember
        ProjectMember projectMember = new ProjectMember();
        projectMember.setProjectId(projectId);
        projectMember.setUserId(userId);
        projectMember.setRole("MEMBER"); // Default role
        projectMember.setJoinedTime(LocalDateTime.now());

        int rows = projectMemberMapper.insertProjectMember(projectMember);
        return rows > 0;
    }

    @Override
    public boolean exitProject(Integer projectId, Integer userId) {
        int rows = projectMemberMapper.deleteProjectMember(projectId, userId);
        return rows > 0;
    }

    @Override
    public List<ProjectMember> listMembersByProjectId(Integer projectId) {
        return projectMemberMapper.findMembersByProjectId(projectId);
    }

    @Override
    public Integer createProject(Project project, Integer creatorUserId) {
        project.setCreatorId(creatorUserId);

        int rows = projectMapper.insertProject(project);

        if (rows <= 0 || project.getId() == null) {
            throw new RuntimeException("Failed to create project.");
        }

        // Insert into project_member table, role=OWNER
        ProjectMember projectMember = new ProjectMember();
        projectMember.setProjectId(project.getId());
        projectMember.setUserId(creatorUserId);
        projectMember.setRole("OWNER");
        projectMember.setJoinedTime(LocalDateTime.now());

        int memberRows = projectMemberMapper.insertProjectMember(projectMember);

        if (memberRows <= 0) {
            throw new RuntimeException("Failed to assign creator as project owner.");
        }

        return project.getId();
    }

    @Override
    public boolean deleteProject(Integer projectId, Integer userId) {
        // Check if the user is the OWNER
        ProjectMember projectMember = projectMemberMapper.findProjectMember(projectId, userId);
        if (projectMember == null || !"OWNER".equalsIgnoreCase(projectMember.getRole())) {
            throw new RuntimeException("Only the project owner can delete the project.");
        }

        // Step 1: Delete all members first
        projectMemberMapper.deleteAllMembersByProjectId(projectId);

        // Step 2: Delete the project itself
        int rows = projectMapper.deleteProject(projectId);

        return rows > 0;
    }
}
