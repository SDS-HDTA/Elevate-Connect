package org.example.codesignconnect.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.example.codesignconnect.model.Project;

import java.util.List;

@Mapper
public interface ProjectMapper {

    List<Project> getAllProjects();

    Project getProjectById(Integer id);

    List<Project> findProjectsByIds(@Param("projectIds") List<Integer> projectIds);

    int insertProject(Project project);

    int updateProject(Project project);

    int deleteProject(Integer id);

    List<Project> searchProjects(@Param("name") String name,
                                 @Param("category") String category,
                                 @Param("creatorId") Integer creatorId,
                                 @Param("status") Integer status);

    List<Project> findProjectsByUserId(Integer userId);
}