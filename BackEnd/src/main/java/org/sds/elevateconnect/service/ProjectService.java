package org.sds.elevateconnect.service;

import lombok.extern.slf4j.Slf4j;
import org.sds.elevateconnect.dto.CreateProjectRequest;
import org.sds.elevateconnect.dto.ProjectResponse;
import org.sds.elevateconnect.dto.UserDetail;
import org.sds.elevateconnect.exceptions.ProjectException;
import org.sds.elevateconnect.mapper.ProjectMapper;
import org.sds.elevateconnect.mapper.ProjectMemberMapper;
import org.sds.elevateconnect.model.Community;
import org.sds.elevateconnect.model.PageResult;
import org.sds.elevateconnect.model.auth.UserRole;
import org.sds.elevateconnect.model.project.Iteration;
import org.sds.elevateconnect.model.project.Project;
import org.sds.elevateconnect.model.project.ProjectCategory;
import org.sds.elevateconnect.model.project.ProjectStage;
import org.sds.elevateconnect.service.interfaces.IProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.sds.elevateconnect.utils.Constants.INITIAL_PROJECT_STAGE;

@Slf4j
@Service
public class ProjectService implements IProjectService {
    @Autowired
    private UserService userService;
    @Autowired
    private CommunityService communityService;
    @Autowired
    private IterationService iterationService;
    @Autowired
    private ProjectMapper projectMapper;
    @Autowired
    private ProjectMemberMapper projectMemberMapper;

    @Override
    public void createProject(CreateProjectRequest createProjectRequest) {
        // Check if project name is already in use
        if (!searchProjectByName(createProjectRequest.name()).isEmpty()) {
            throw new ProjectException("Project name is already taken.");
        }

        if (userService.getUserRoleById(createProjectRequest.creatorId()) != UserRole.ELEVATE_FACILITATION_LEAD) {
            throw new ProjectException("User is not allowed to create projects.");
        }

        try {
            Project newProject = new Project(
                    null, // Set by DB
                    createProjectRequest.creatorId(),
                    1, // TODO: Temporary static id. Replace this with id of image from DB
                    createProjectRequest.communityId(),
                    createProjectRequest.name(),
                    INITIAL_PROJECT_STAGE,
                    createProjectRequest.description(),
                    ProjectCategory.fromInt(createProjectRequest.category()),
                    LocalDate.parse(createProjectRequest.targetDate()),
                    Timestamp.from(Instant.now())
            );

            projectMapper.createProject(newProject);

            // Register elevate admin as a project member as they created the project
            projectMemberMapper.insertProjectMember(newProject.getId(), createProjectRequest.creatorId());

            // Create new iteration
            // TODO: Make it so this is not just all null
            iterationService.createIteration(
                    new Iteration(
                            null, // Id is set by DB
                            newProject.getId(),
                            INITIAL_PROJECT_STAGE.getIntValue(),
                            null, // Set by default
                            null, // Set by default
                            LocalDate.now(),
                            null, // Null as iteration will not have finished yet
                            null, // Set by DB
                            null // Set by DB
                    )
            );
        } catch (Exception e) {
            log.error(String.valueOf(e));
            throw new ProjectException("Failed to create project.");
        }
    }

    @Override
    public void joinProject(Integer projectId, Integer userId) throws ProjectException {
        Project project = projectMapper.getProjectById(projectId);

        if (project == null) {
            throw new ProjectException("Project not found");
        }

        if (projectMemberMapper.getProjectMember(projectId, userId) != null) {
            throw new ProjectException("User is already member of the project.");
        }

        projectMemberMapper.insertProjectMember(projectId, userId);
    }

    @Override
    public PageResult<ProjectResponse> getPaginatedListOfAllProjects(Integer page, Integer size, Integer searchType, String searchValue) {
        int pageNumber = (page == null || page <= 0) ? 1 : page;
        int sizeOfPage = (size == null || size <= 0) ? 10 : size;
        int offset = (pageNumber - 1) * sizeOfPage;

        if (searchValue != null && !searchValue.trim().isEmpty()) {
            searchValue = "%" + searchValue.trim().toLowerCase() + "%";
        } else {
            searchValue = null;
            searchType = null;
        }

        List<Project> projects = projectMapper.getPaginatedListOfProjects(offset, sizeOfPage, searchType, searchValue);

        Long count = projectMapper.countProjectsBySearch(searchType, searchValue);

        return new PageResult<>(count, mapProjectsToProjectResponses(projects));
    }

    @Override
    public ProjectResponse getProjectById(Integer projectId) throws ProjectException {
        Project project = projectMapper.getProjectById(projectId);
        if (project == null) {
            throw new ProjectException("Project not found");
        }

        return mapProjectToProjectResponse(project);
    }

    @Override
    public List<ProjectResponse> searchMyProjects(Integer userId, Integer searchType, String searchValue) {
        String searchString;

        if (searchValue != null && !searchValue.trim().isEmpty()) {
            searchString = "%" + searchValue.toLowerCase() + "%";
        } else {
            searchString = null;
        }

        List<Project> projects = projectMapper.getMyProjectsBySearch(userId, searchType, searchString);

        return mapProjectsToProjectResponses(projects);
    }

    @Override
    public int getProjectStage(Integer projectId) {
        return projectMapper.getProjectStage(projectId);
    }

    @Override
    public List<ProjectResponse> getAllProjects() {
        List<Project> projects = projectMapper.getAllProjects();

        return mapProjectsToProjectResponses(projects);
    }

    @Override
    public int getMemberCount(Integer projectId) {
        return projectMemberMapper.getMembersByProjectId(projectId).size();
    }

    @Override
    public List<ProjectResponse> searchProjects(String name, String category, Integer creatorId, Integer status) {
        List<Project> projects = projectMapper.searchProjects(name, category, creatorId, status);
        return mapProjectsToProjectResponses(projects);
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
    public List<ProjectResponse> searchProjectByName(String name) {
        List<Project> projects = projectMapper.searchByName("%" + name.toLowerCase() + "%");
        return mapProjectsToProjectResponses(projects);
    }

    @Override
    public void update(Project project) {
        try {
            projectMapper.updateProject(project);
        } catch (Exception e) {
            log.error(String.valueOf(e));
            throw new ProjectException("Failed to update project.");
        }
    }

    @Override
    public void updateProjectStage(Integer projectId, Integer stage) {
        ProjectStage projectStage;

        try {
            projectStage = ProjectStage.fromInt(stage);
        } catch (Exception e) {
            throw new ProjectException("Invalid project stage.");
        }

        try {
            projectMapper.updateProjectStage(projectId, projectStage);
        } catch (Exception e) {
            throw new ProjectException("Failed to update project stage.");
        }
    }

    @Override
    public void removeMemberFromProject(Integer projectId, Integer userId) {
        projectMemberMapper.deleteProjectMember(projectId, userId);
    }

    @Override
    public void deleteProject(Integer projectId) {
        // Step 1: Delete all members first
        projectMemberMapper.deleteAllMembersByProjectId(projectId);
        // Step 2: Delete the project itself
        projectMapper.deleteProjectById(projectId);
    }

    private ProjectResponse mapProjectToProjectResponse(Project project) {
        List<UserDetail> members = projectMemberMapper.getMembersByProjectId(project.getId());
        Community community = communityService.getCommunityById(project.getCommunityId());

        return new ProjectResponse(project, members, community);
    }

    private List<ProjectResponse> mapProjectsToProjectResponses(List<Project> projects) {
        List<ProjectResponse> projectResponses = new ArrayList<>();

        for (Project project : projects) {
            projectResponses.add(mapProjectToProjectResponse(project));
        }

        return projectResponses;
    }
}
