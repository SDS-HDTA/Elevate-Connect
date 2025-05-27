package org.example.codesignconnect.controller;

import org.example.codesignconnect.model.Result;
import org.example.codesignconnect.model.Task;
import org.example.codesignconnect.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class TaskController {

    @Autowired
    private TaskService taskService;

    @PostMapping("/projects/{projectId}/tasks")
    public Result createTask(@RequestBody Task task) {
        return Result.success(taskService.createTask(task));
    }

    @PutMapping("/task")
    public Result updateTask(@RequestBody Task task) {
        return Result.success(taskService.updateTask(task));
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
