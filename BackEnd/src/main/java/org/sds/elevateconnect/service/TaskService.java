package org.sds.elevateconnect.service;

import org.sds.elevateconnect.model.project.Task;

import java.util.List;

public interface TaskService {
    int createTask(Task task);
    int updateTask(Task task);
    int deleteTask(Integer id);
    Task getTaskById(Integer id);
    List<Task> getTasksByIterationId(Integer iterationId);
}