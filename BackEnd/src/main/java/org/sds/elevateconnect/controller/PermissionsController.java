package org.sds.elevateconnect.controller;

import org.sds.elevateconnect.model.Result;
import org.sds.elevateconnect.service.PermissionsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/permissions")
public class PermissionsController {
    @Autowired
    PermissionsService permissionsService;

    @GetMapping
    public Result getMapOfRoleToPermissions() {
        return Result.success(permissionsService.getMapOfRoleToPermissions());
    }
}
