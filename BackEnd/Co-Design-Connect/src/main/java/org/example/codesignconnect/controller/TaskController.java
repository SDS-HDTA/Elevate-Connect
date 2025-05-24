package org.example.codesignconnect.controller;

import org.example.codesignconnect.model.Result;
import org.example.codesignconnect.model.Task;
import org.example.codesignconnect.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/tasks")
public class TaskController {

    @Autowired
    private TaskService taskService;

    @PostMapping
    public Result createTask(@RequestBody Task task) {
        return Result.success(taskService.createTask(task));
    }

    @PutMapping
    public Result updateTask(@RequestBody Task task) {
        return Result.success(taskService.updateTask(task));
    }

    @DeleteMapping("/{id}")
    public Result deleteTask(@PathVariable Integer id) {
        return Result.success(taskService.deleteTask(id));
    }

    @GetMapping("/{id}")
    public Result getTask(@PathVariable Integer id) {
        return Result.success(taskService.getTaskById(id));
    }

    @GetMapping("/iteration/{iterationId}")
    public Result listTasks(@PathVariable Integer iterationId) {
        return Result.success(taskService.getTasksByIterationId(iterationId));
    }
}
