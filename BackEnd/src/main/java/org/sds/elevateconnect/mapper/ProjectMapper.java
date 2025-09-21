package org.sds.elevateconnect.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.sds.elevateconnect.model.project.Project;
import org.sds.elevateconnect.model.project.ProjectCategory;
import org.sds.elevateconnect.model.project.ProjectStage;

import java.time.LocalDate;
import java.util.List;

@Mapper
public interface ProjectMapper {
    Project createProject(int creatorId, Integer projectImageId, int communityId, String name, ProjectStage currentStage, String description, ProjectCategory category, LocalDate targetDate);
    List<Project> getProjectsByCreatorId(Integer userId);
    Project getProjectById(Integer id);
    ProjectStage getProjectStage(Integer projectId);
    List<Project> getAllProjects();
    int updateProject(Project project);
    int updateProjectStage(Integer projectId, ProjectStage stage);
    int deleteProject(Integer id);
    int deleteProjectById(Integer projectId);

    List<Project> getPaginatedListOfProjects(@Param("offset") int offset,
                                             @Param("size") int size,
                                             @Param("searchType") Integer searchType,
                                             @Param("searchValue") String searchValue);

    Long countProjectsBySearch(@Param("searchType") Integer searchType,
                               @Param("searchValue") String searchValue);

    List<Project> getMyProjectsBySearch(@Param("userId") Integer userId,
                                        @Param("searchType") Integer searchType,
                                        @Param("searchValue") String searchValue);

    List<Project> searchProjects(@Param("name") String name,
                                 @Param("category") String category,
                                 @Param("creatorId") Integer creatorId,
                                 @Param("status") Integer status);

    List<Project> searchByName(@Param("name") String name);
}