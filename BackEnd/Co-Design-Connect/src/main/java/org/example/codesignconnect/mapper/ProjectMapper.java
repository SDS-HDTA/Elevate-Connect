package org.example.codesignconnect.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.example.codesignconnect.model.Project;

import java.util.List;

@Mapper
public interface ProjectMapper {

    // 获取所有未被删除的项目
    List<Project> getAllProjects();

    // 通过 ID 获取项目详情
    Project getProjectById(Integer id);

    // 插入一个新项目
    int insertProject(Project project);

    // 更新项目
    int updateProject(Project project);

    // 删除项目（逻辑删除）
    int deleteProjectById(Integer id);
}