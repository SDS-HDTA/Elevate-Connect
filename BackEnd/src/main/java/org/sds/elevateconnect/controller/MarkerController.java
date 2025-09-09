package org.sds.elevateconnect.controller;

import org.sds.elevateconnect.model.Marker;
import org.sds.elevateconnect.model.Result;
import org.sds.elevateconnect.service.MarkerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/markers")
public class MarkerController {
    @Autowired
    private MarkerService markerService;

    @GetMapping
    public Result getAllByProjectId(Integer projectId) {
        return Result.success(markerService.findAllByProjectId(projectId));
    }

    @PostMapping("/create")
    public Result create(@RequestBody Marker marker) {
        markerService.insert(marker);
        return Result.success(marker);
    }

    @PutMapping("/{id}")
    public Result update(@RequestBody Marker marker, @PathVariable Integer id) {
        marker.setId(id);
        markerService.update(marker);
        return Result.success();
    }

    @DeleteMapping("/{projectId}/{id}")
    public Result delete(@PathVariable Integer projectId, @PathVariable Integer id) {
        markerService.delete(id);
        return Result.success();
    }
}
