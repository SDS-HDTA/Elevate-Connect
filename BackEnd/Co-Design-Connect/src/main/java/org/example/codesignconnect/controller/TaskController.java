package org.example.codesignconnect.controller;

import org.example.codesignconnect.dto.TaskDetail;
import org.example.codesignconnect.mapper.TaskMapper;
import org.example.codesignconnect.mapper.UserMapper;
import org.example.codesignconnect.model.Result;
import org.example.codesignconnect.model.Task;
import org.example.codesignconnect.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@RestController
public class TaskController {

    @Autowired
    private TaskService taskService;

    @Autowired
    private UserMapper userMapper;

    @PostMapping("/projects/{projectId}/tasks")
    public Result createTask(@RequestBody Task task, @PathVariable Integer projectId) {
        task.setProjectId(projectId);
        int rows = taskService.createTask(task);
        if (rows > 0) {
            String name = userMapper.getUsernameById(task.getCreatorId());
            TaskDetail taskDetail = new TaskDetail(new ArrayList<>(), task, name, null);
            return Result.success(taskDetail);
        }
        else return Result.error("Failed");
    }

    @PutMapping("/projects/{projectId}/tasks/{id}")
    public Result updateTask(@RequestBody Task task, @PathVariable Integer projectId, @PathVariable Integer id) {
        task.setProjectId(projectId);
        task.setId(id);
        int rows = taskService.updateTask(task);
        if (rows > 0) {
            TaskDetail taskDetail = new TaskDetail(new ArrayList<>(), taskService.getTaskById(task.getId()), null, null);
            return Result.success(taskDetail);
        }
        else return Result.error("Failed");
    }

    @DeleteMapping("/projects/{projectId}/tasks/{taskId}")
    public Result deleteTask(@PathVariable Integer taskId) {
        return Result.success(taskService.deleteTask(taskId));
    }

    @GetMapping("/projects/{projectId}/tasks/{taskId}")
    public Result getTask(@PathVariable Integer taskId) {
        return Result.success(taskService.getTaskById(taskId));
    }

    @GetMapping("/iteration/{iterationId}")
    public Result listTasks(@PathVariable Integer iterationId) {
        return Result.success(taskService.getTasksByIterationId(iterationId));
    }
}
