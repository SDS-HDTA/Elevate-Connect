package org.example.codesignconnect.controller;

import org.example.codesignconnect.model.Iteration;
import org.example.codesignconnect.model.Result;
import org.example.codesignconnect.service.IterationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/iterations")
public class IterationController {

    @Autowired
    private IterationService iterationService;

    @PostMapping
    public Result createIteration(@RequestBody Iteration iteration) {
        return Result.success(iterationService.createIteration(iteration));
    }

    @PutMapping
    public Result updateIteration(@RequestBody Iteration iteration) {
        return Result.success(iterationService.updateIteration(iteration));
    }

    @DeleteMapping("/{id}")
    public Result deleteIteration(@PathVariable Integer id) {
        return Result.success(iterationService.deleteIteration(id));
    }

    @GetMapping("/{id}")
    public Result getIteration(@PathVariable Integer id) {
        return Result.success(iterationService.getIterationById(id));
    }

    @GetMapping("/project/{projectId}")
    public Result listIterations(@PathVariable Integer projectId) {
        return Result.success(iterationService.getIterationsByProjectId(projectId));
    }
}
