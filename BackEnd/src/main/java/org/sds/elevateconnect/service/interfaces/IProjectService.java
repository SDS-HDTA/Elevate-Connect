package org.sds.elevateconnect.service.interfaces;

import org.sds.elevateconnect.dto.CreateProjectRequest;
import org.sds.elevateconnect.dto.ProjectResponse;
import org.sds.elevateconnect.dto.UpdateProjectRequest;
import org.sds.elevateconnect.dto.UserDetail;
import org.sds.elevateconnect.model.PageResult;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface IProjectService {
    void addUsersToProject(Integer projectId, List<Integer> userIds);
    void createProject(CreateProjectRequest createProjectRequest, MultipartFile projectImage);
    PageResult<ProjectResponse> getPaginatedListOfAllProjects(Integer userId, Integer page, Integer size, Integer searchType, String searchValue);
    ProjectResponse getProjectById(Integer projectId);
    List<ProjectResponse> searchMyProjects(Integer userId, Integer searchType, String searchValue);
    int getProjectStage(Integer projectId);
    List<ProjectResponse> getAllProjects();
    int getMemberCount(Integer projectId);
    List<ProjectResponse> searchProjects(String name, String category, Integer creatorId, Integer status);
    List<UserDetail> listMembersByProjectId(Integer projectId);
    List<ProjectResponse> searchProjectByName(String name);
    void updateProject(Integer projectId, UpdateProjectRequest updateProjectRequest, MultipartFile projectImage);
    void updateProjectStage(Integer projectId, Integer stage);
    void removeMemberFromProject(Integer projectId, Integer userId);
    void deleteProject(Integer projectId);
}
