package org.sds.elevateconnect.service;

import org.sds.elevateconnect.dto.CreateProjectRequest;
import org.sds.elevateconnect.dto.ProjectResponse;
import org.sds.elevateconnect.dto.UserDetail;
import org.sds.elevateconnect.exceptions.ProjectException;
import org.sds.elevateconnect.mapper.ProjectMapper;
import org.sds.elevateconnect.mapper.ProjectMemberMapper;
import org.sds.elevateconnect.mapper.UserMapper;
import org.sds.elevateconnect.model.PageResult;
import org.sds.elevateconnect.model.project.Project;
import org.sds.elevateconnect.model.project.ProjectCategory;
import org.sds.elevateconnect.model.project.ProjectStage;
import org.sds.elevateconnect.service.interfaces.IProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

import static org.sds.elevateconnect.utils.Constants.INITIAL_PROJECT_STAGE;

@Service
public class ProjectService implements IProjectService {
    @Autowired
    private ProjectMapper projectMapper;
    @Autowired
    private ProjectMemberMapper projectMemberMapper;
    @Autowired
    private UserMapper userMapper;

    @Override
    public Project createProject(CreateProjectRequest createProjectRequest) {
        Project newProject = projectMapper.createProject(
                createProjectRequest.creatorId(),
                null,
                createProjectRequest.communityId(),
                createProjectRequest.name(),
                INITIAL_PROJECT_STAGE,
                createProjectRequest.description(),
                ProjectCategory.fromInt(createProjectRequest.category()),
                LocalDate.parse(createProjectRequest.targetDate())
        );

        projectMemberMapper.insertProjectMember(newProject.getId(), createProjectRequest.creatorId());

        return newProject;
    }

    @Override
    public void joinProject(Integer projectId, Integer userId) throws ProjectException {
        Project project = projectMapper.getProjectById(projectId);

        if (project == null) {
            throw new ProjectException("Project not found");
        }

        if (projectMemberMapper.getProjectMember(projectId, userId) == null) {
            throw new ProjectException("User is not a member of the project.");
        }

        projectMemberMapper.insertProjectMember(projectId, userId);
    }

    @Override
    public PageResult<Project> getPaginatedListOfAllProjects(Integer page, Integer size, Integer searchType, String searchValue) {
        int pageNumber = (page == null || page <= 0) ? 1 : page;
        int sizeOfPage = (size == null || size <= 0) ? 10 : size;
        int offset = (pageNumber - 1) * sizeOfPage;

        String searchString;

        if (searchValue != null && !searchValue.trim().isEmpty()) {
            searchString = "%" + searchValue.trim().toLowerCase() + "%";
        } else {
            searchString = null;
        }

        List<Project> projects = projectMapper.getPaginatedListOfProjects(offset, sizeOfPage, searchType, searchString);
        Long count = projectMapper.countProjectsBySearch(searchType, searchString);

        return new PageResult<>(count, projects);
    }

    @Override
    public ProjectResponse getProjectById(Integer projectId) throws ProjectException {
        Project project = projectMapper.getProjectById(projectId);
        if (project == null) {
            throw new ProjectException("Project not found");
        }

        List<UserDetail> members = projectMemberMapper.getMembersByProjectId(projectId);

        return new ProjectResponse(project, members);
    }

    @Override
    public List<Project> searchMyProjects(Integer userId, Integer searchType, String searchValue) {
        String searchString;

        if (searchValue != null && !searchValue.trim().isEmpty()) {
            searchString = "%" + searchValue.toLowerCase() + "%";
        } else {
            searchString = null;
        }

        return projectMapper.getMyProjectsBySearch(userId, searchType, searchString);
    }

    @Override
    public ProjectStage getProjectStage(Integer projectId) {
        return projectMapper.getProjectStage(projectId);
    }

    @Override
    public List<Project> getAllProjects() {
        return projectMapper.getAllProjects();
    }

    @Override
    public int getMemberCount(Integer projectId) {
        return projectMemberMapper.getMembersByProjectId(projectId).size();
    }

    @Override
    public List<Project> searchProjects(String name, String category, Integer creatorId, Integer status) {
        return projectMapper.searchProjects(name, category, creatorId, status);
    }

    @Override
    public List<UserDetail> listMembersByProjectId(Integer projectId) {
        Project project = projectMapper.getProjectById(projectId);

        if (project == null) {
            throw new ProjectException("Project not found");
        }

        return projectMemberMapper.getMembersByProjectId(projectId);
    }

    @Override
    public List<Project> searchProjectByName(String name) {
        return projectMapper.searchByName("%" + name.toLowerCase() + "%");
    }

    @Override
    public void update(Project project) {
        projectMapper.updateProject(project);
    }

    @Override
    public void updateProjectStage(Integer projectId, Integer stage) {
        /*
        Integer status = requestBody.get("status");
        if (status == null || status < 0 || status > 5) {
            return Result.error("Invalid status");
        }

        int result = projectService.updateProjectStage(projectId, status);
        if (result > 0) {
            return Result.success(null);
        } else {
            return Result.error("Failed to update project status");
        }
        * */
        ProjectStage projectStage;

        try {
            projectStage = ProjectStage.fromInt(stage);
        } catch (Exception e) {
            throw new ProjectException("Invalid project stage.");
        }

        // Update
        projectMapper.updateProjectStage(projectId, projectStage);
    }

    @Override
    public void removeMemberFromProject(Integer projectId, Integer userId) {
        projectMemberMapper.deleteProjectMember(projectId, userId);
    }

    @Override
    public void deleteProject(Integer projectId, Integer userId) {
        // Step 1: Delete all members first
        projectMemberMapper.deleteAllMembersByProjectId(projectId);

        // Step 2: Delete the project itself
        int rows = projectMapper.deleteProject(projectId);
    }
}
