package org.sds.elevateconnect.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.sds.elevateconnect.dto.UserDetail;

import java.util.List;

/**
 * Mapper interface for operations related to project members.
 */
@Mapper
public interface ProjectMemberMapper {
    void insertProjectMember(int projectId, int userId);
    UserDetail getProjectMember(@Param("projectId") Integer projectId, @Param("userId") Integer userId);
    List<UserDetail> getMembersByProjectId(@Param("projectId") Integer projectId);
    void deleteProjectMember(@Param("projectId") Integer projectId, @Param("userId") Integer userId);
    void deleteAllMembersByProjectId(@Param("projectId") Integer projectId);
}