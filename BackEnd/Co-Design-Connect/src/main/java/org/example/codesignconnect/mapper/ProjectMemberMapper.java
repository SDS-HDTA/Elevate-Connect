package org.example.codesignconnect.mapper;

import org.example.codesignconnect.model.ProjectMember;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Mapper interface for operations related to project members.
 */
@Mapper
public interface ProjectMemberMapper {

    /**
     * Retrieve a list of project IDs that a user has joined.
     *
     * @param userId the ID of the user
     * @return list of project IDs
     */
    List<Integer> findProjectIdsByUserId(@Param("userId") Integer userId);

    /**
     * Insert a new member into a project.
     *
     * @param projectMember the project member entity
     * @return number of rows affected
     */
    int insertProjectMember(ProjectMember projectMember);

    /**
     * Delete a member from a project (exit project).
     *
     * @param projectId the project ID
     * @param userId the user ID
     * @return number of rows affected
     */
    int deleteProjectMember(@Param("projectId") Integer projectId, @Param("userId") Integer userId);

    /**
     * Find a specific project member by project ID and user ID.
     *
     * @param projectId the project ID
     * @param userId the user ID
     * @return the project member entity
     */
    ProjectMember findProjectMember(@Param("projectId") Integer projectId, @Param("userId") Integer userId);

    /**
     * Retrieve all members of a project.
     *
     * @param projectId the ID of the project
     * @return list of project member entities
     */
    List<ProjectMember> findMembersByProjectId(@Param("projectId") Integer projectId);

    /**
     * Delete all members associated with a project (used when deleting a project).
     *
     * @param projectId the project ID
     * @return number of rows affected
     */
    int deleteAllMembersByProjectId(@Param("projectId") Integer projectId);
}