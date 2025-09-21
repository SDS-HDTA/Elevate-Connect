package org.sds.elevateconnect.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.sds.elevateconnect.dto.UserDetail;
import org.sds.elevateconnect.model.project.ProjectMember;

import java.util.List;

/**
 * Mapper interface for operations related to project members.
 */
@Mapper
public interface ProjectMemberMapper {
    int insertProjectMember(int projectId, int userId);
    ProjectMember getProjectMember(@Param("projectId") Integer projectId, @Param("userId") Integer userId);
    List<Integer> getProjectIdsByUserId(@Param("userId") Integer userId);
    List<UserDetail> getMembersByProjectId(@Param("projectId") Integer projectId);
    int countMembers(@Param("projectId") Integer projectId);
    int deleteProjectMember(@Param("projectId") Integer projectId, @Param("userId") Integer userId);
    void deleteAllMembersByProjectId(@Param("projectId") Integer projectId);
}