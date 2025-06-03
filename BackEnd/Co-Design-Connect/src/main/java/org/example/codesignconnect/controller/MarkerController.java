package org.example.codesignconnect.controller;

import org.example.codesignconnect.model.Marker;
import org.example.codesignconnect.model.Result;
import org.example.codesignconnect.service.MarkerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/markers")
public class MarkerController {
    @Autowired
    private MarkerService markerService;

    @GetMapping("/{projectId}")
    public Result getAllByProjectId(@PathVariable Integer projectId) {
        return Result.success(markerService.findAllByProjectId(projectId));
    }

    @PostMapping
    public Result create(@RequestBody Marker marker) {
        markerService.insert(marker);
        return Result.success();
    }

    @PutMapping
    public Result update(@RequestBody Marker marker) {
        markerService.update(marker);
        return Result.success();
    }

    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Integer id) {
        markerService.delete(id);
        return Result.success();
    }
}
