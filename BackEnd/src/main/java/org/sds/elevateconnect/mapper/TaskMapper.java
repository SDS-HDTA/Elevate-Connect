package org.sds.elevateconnect.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.sds.elevateconnect.model.Task;

import java.util.List;

@Mapper
public interface TaskMapper {
    int insertTask(Task task);
    int updateTask(Task task);
    int deleteTask(@Param("id") Integer id);
    Task getTaskById(@Param("id") Integer id);
    List<Task> getTasksByIterationId(@Param("iterationId") Integer iterationId);
    List<Task> getMainTasksByIterationId(Integer iterationId);
    List<Task> getSubTasksByTaskId(Integer taskId);
    int getCountByCode(String code);
}