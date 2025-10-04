package org.sds.elevateconnect.controller;

import org.sds.elevateconnect.model.auth.Permission;
import org.sds.elevateconnect.model.Result;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/permissions")
public class PermissionsController {
    @GetMapping
    public Result getMapOfRoleToPermissions() {
        return Result.success(Permission.getRolePermissionsMap());
    }
}
