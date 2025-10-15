package org.sds.elevateconnect.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.sds.elevateconnect.model.auth.UserRole;
import org.sds.elevateconnect.model.project.Project;
import org.sds.elevateconnect.model.project.ProjectStage;

import java.util.List;

@Mapper
public interface ProjectMapper {
    void createProject(Project newProject);
    Project getProjectById(Integer id);
    int getProjectStage(Integer projectId);
    List<Project> getAllProjects();
    List<Project> getPaginatedListOfProjects(@Param("userId") Integer userId,
                                             @Param("offset") int offset,
                                             @Param("size") int size,
                                             @Param("searchType") Integer searchType,
                                             @Param("searchValue") String searchValue);
    Long countProjectsBySearch(@Param("userId") Integer userId,
                               @Param("searchType") Integer searchType,
                               @Param("searchValue") String searchValue);
    List<Project> getMyProjectsBySearch(@Param("userId") Integer userId,
                                        @Param("searchType") Integer searchType,
                                        @Param("searchValue") String searchValue,
                                        @Param("userRole") UserRole userRole);
    List<Project> searchProjects(@Param("name") String name,
                                 @Param("category") String category,
                                 @Param("creatorId") Integer creatorId,
                                 @Param("stage") Integer stage);
    List<Project> searchByName(@Param("name") String name);
    void updateProject(Project project);
    void updateProjectStage(Integer projectId, ProjectStage stage);
    void deleteProjectById(Integer projectId);
}