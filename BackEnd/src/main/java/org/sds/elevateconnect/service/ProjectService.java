package org.sds.elevateconnect.service;

import lombok.extern.slf4j.Slf4j;
import org.sds.elevateconnect.dto.CreateProjectRequest;
import org.sds.elevateconnect.dto.FileUploadRequest;
import org.sds.elevateconnect.dto.ProjectResponse;
import org.sds.elevateconnect.dto.UserDetail;
import org.sds.elevateconnect.exceptions.ProjectException;
import org.sds.elevateconnect.mapper.ProjectMapper;
import org.sds.elevateconnect.mapper.ProjectMemberMapper;
import org.sds.elevateconnect.model.Community;
import org.sds.elevateconnect.model.PageResult;
import org.sds.elevateconnect.model.auth.UserRole;
import org.sds.elevateconnect.model.project.*;
import org.sds.elevateconnect.service.interfaces.IFileService;
import org.sds.elevateconnect.service.interfaces.IGcsService;
import org.sds.elevateconnect.service.interfaces.IProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

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
    @Autowired
    private IFileService fileService;

    @Override
    public void createProject(CreateProjectRequest createProjectRequest, MultipartFile projectImage) {
        // Check if project name is already in use
        if (!searchProjectByName(createProjectRequest.getName()).isEmpty()) {
            throw new ProjectException("Project name is already taken.");
        }

        if (userService.getUserRoleById(createProjectRequest.getCreatorId()) != UserRole.ELEVATE_FACILITATION_LEAD) {
            throw new ProjectException("User is not allowed to create projects.");
        }

        if (projectImage == null) {
            throw new ProjectException("Project image is required.");
        }

        try {
            Project newProject = new Project(
                    null, // Set by DB
                    createProjectRequest.getCreatorId(),
                    null, // Set after creating project image
                    createProjectRequest.getCommunityId(),
                    createProjectRequest.getName(),
                    INITIAL_PROJECT_STAGE,
                    createProjectRequest.getDescription(),
                    ProjectCategory.fromInt(createProjectRequest.getCategory()),
                    LocalDate.parse(createProjectRequest.getTargetDate()),
                    Timestamp.from(Instant.now())
            );

            // Upload project image and set the generated id in the project object
            File projectImageFile = fileService.addProjectImage(newProject, projectImage);
            newProject.setProjectImageId(projectImageFile.getId());

            projectMapper.createProject(newProject);

            // Register elevate admin as a project member as they created the project
            projectMemberMapper.insertProjectMember(newProject.getId(), createProjectRequest.getCreatorId());

            // Create new iteration
            // TODO: Make it so this is not just all null
            Integer iterationId = iterationService.createIteration(
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
    public PageResult<ProjectResponse> getPaginatedListOfAllProjects(Integer userId, Integer page, Integer size, Integer searchType, String searchValue) {
        int pageNumber = (page == null || page <= 0) ? 1 : page;
        int sizeOfPage = (size == null || size <= 0) ? 10 : size;
        int offset = (pageNumber - 1) * sizeOfPage;

        if (searchValue != null && !searchValue.trim().isEmpty()) {
            searchValue = "%" + searchValue.trim().toLowerCase() + "%";
        } else {
            searchValue = null;
            searchType = null;
        }

        List<Project> projects = projectMapper.getPaginatedListOfProjects(userId, offset, sizeOfPage, searchType, searchValue);

        Long count = projectMapper.countProjectsBySearch(userId, searchType, searchValue);

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

        UserRole userRole = userService.getUserRoleById(userId);

        List<Project> projects = projectMapper.getMyProjectsBySearch(userId, searchType, searchString, userRole);

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
        String projectImageSrc = fileService.getFileById(project.getProjectImageId()).getSource();

        return new ProjectResponse(project, projectImageSrc, members, community);
    }

    private List<ProjectResponse> mapProjectsToProjectResponses(List<Project> projects) {
        List<ProjectResponse> projectResponses = new ArrayList<>();

        for (Project project : projects) {
            projectResponses.add(mapProjectToProjectResponse(project));
        }

        return projectResponses;
    }
}
