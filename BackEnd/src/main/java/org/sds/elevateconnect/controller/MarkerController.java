package org.sds.elevateconnect.controller;

import org.sds.elevateconnect.config.security.RequirePermission;
import org.sds.elevateconnect.dto.CreateMarkerRequest;
import org.sds.elevateconnect.dto.UpdateMarkerRequest;
import org.sds.elevateconnect.model.auth.Permission;
import org.sds.elevateconnect.model.project.Marker;
import org.sds.elevateconnect.model.Result;
import org.sds.elevateconnect.service.interfaces.IMarkerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/projects/{projectId}/markers")
public class MarkerController {
    @Autowired
    private IMarkerService markerService;

    @RequirePermission(Permission.ACCESS_MAP_PAGE)
    @GetMapping
    public Result getAllByProjectId(@PathVariable Integer projectId) {
        return Result.success(markerService.getAllByProjectId(projectId));
    }

    @RequirePermission(Permission.CREATE_MAP_MARKER)
    @PostMapping
    public Result create(@RequestBody CreateMarkerRequest request, @PathVariable Integer projectId) {
        request.setProjectId(projectId);
        Marker marker = markerService.insert(request);
        return Result.success(marker);
    }

    @RequirePermission(Permission.EDIT_MAP_MARKER)
    @PutMapping
    public Result update(@RequestBody UpdateMarkerRequest request) {
        markerService.update(request);
        return Result.success();
    }

    @RequirePermission(Permission.DELETE_MAP_MARKER)
    @DeleteMapping
    public Result delete(@RequestParam Integer id) {
        markerService.delete(id);
        return Result.success();
    }
}
