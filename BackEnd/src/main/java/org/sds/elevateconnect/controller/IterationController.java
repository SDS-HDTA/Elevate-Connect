package org.sds.elevateconnect.controller;

import lombok.extern.slf4j.Slf4j;
import org.sds.elevateconnect.config.security.RequirePermission;
import org.sds.elevateconnect.model.auth.Permission;
import org.sds.elevateconnect.model.project.Iteration;
import org.sds.elevateconnect.model.Result;
import org.sds.elevateconnect.service.IterationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
public class IterationController {

    @Autowired
    private IterationService iterationService;

    @RequirePermission(Permission.MODIFY_CURRENT_PROJECT_STAGE)
    @PostMapping("/projects/{projectId}/iterations")
    public Result createIteration(@RequestBody Iteration iteration, @PathVariable Integer projectId) {
        log.info("Create iteration");
        iteration.setProjectId(projectId);
        return Result.success(iterationService.createIteration(iteration));
    }

    @RequirePermission(Permission.MODIFY_CURRENT_PROJECT_STAGE)
    @PutMapping("/iteration")
    public Result updateIteration(@RequestBody Iteration iteration) {
        log.info("Update iteration");
        return Result.success(iterationService.updateIteration(iteration));
    }

    @RequirePermission(Permission.MODIFY_CURRENT_PROJECT_STAGE)
    @DeleteMapping("/projects/{projectId}/iterations")
    public Result deleteIteration(@PathVariable Integer projectId) {
        log.info("Delete iteration");
        return Result.success(iterationService.deleteIteration(projectId));
    }

    @RequirePermission(Permission.ACCESS_ACTIVITIES_PAGE)
    @GetMapping("/projects/{projectId}/iterations")
    public Result getIterations(@PathVariable Integer projectId, @RequestParam Short status) {
        log.info("Get iteration");
        return Result.success(iterationService.getIterations(projectId, status.intValue()));
    }

    @RequirePermission(Permission.ACCESS_RESOURCES_PAGE)
    @GetMapping("/projects/{projectId}/folders")
    public Result getFolders(@PathVariable Integer projectId) {
        return Result.success(iterationService.getFolders(projectId));
    }
}
