package org.example.codesignconnect.controller;

import org.example.codesignconnect.model.Iteration;
import org.example.codesignconnect.model.Result;
import org.example.codesignconnect.service.IterationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class IterationController {

    @Autowired
    private IterationService iterationService;

    @PostMapping("/projects/{projectId}/iterations")
    public Result createIteration(@RequestBody Iteration iteration) {
        return Result.success(iterationService.createIteration(iteration));
    }

    @PutMapping("/iteration")
    public Result updateIteration(@RequestBody Iteration iteration) {
        return Result.success(iterationService.updateIteration(iteration));
    }

    @DeleteMapping("/projects/{projectId}/iterations")
    public Result deleteIteration(@PathVariable Integer projectId) {
        return Result.success(iterationService.deleteIteration(projectId));
    }

    @GetMapping("/projects/{projectId}/iterations")
    public Result getIteration(@PathVariable Integer projectId, @RequestParam Integer status) {
        return Result.success(iterationService.getIterations(projectId, status));
    }

    @GetMapping("/project/{projectId}")
    public Result listIterations(@PathVariable Integer projectId) {
        return Result.success(iterationService.getIterationsByProjectId(projectId));
    }
}
