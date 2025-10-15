package org.sds.elevateconnect.controller;

import jakarta.annotation.Nullable;
import lombok.extern.slf4j.Slf4j;
import org.sds.elevateconnect.config.security.RequirePermission;
import org.sds.elevateconnect.model.Result;
import org.sds.elevateconnect.model.auth.Permission;
import org.sds.elevateconnect.service.InviteCodeService;
import org.sds.elevateconnect.service.ProjectService;
import org.sds.elevateconnect.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
public class ManagerController {
    @Autowired
    private ProjectService projectService;
    @Autowired
    private UserService userService;
    @Autowired
    private InviteCodeService inviteCodeService;

    @RequirePermission(Permission.ACCESS_ADMIN_PANEL_PAGE)
    @GetMapping("/manager/projects")
    public Result getProjectList(){
        return Result.success(projectService.getAllProjects());
    }

    @RequirePermission(Permission.ACCESS_ADMIN_PANEL_PAGE)
    @GetMapping("/manager/users")
    public Result getAllUsers() {
        return Result.success(userService.getAllUsers());
    }

    @RequirePermission(Permission.DELETE_USER)
    @DeleteMapping("/manager/users/{id}")
    public Result deleteUser(@PathVariable Integer id) {
        userService.deleteUser(id);
        return Result.success();
    }

    @RequirePermission(Permission.INVITE_USER)
    @PostMapping("/manager/sendInvitationCode")
    public Result sendInviteCode(String email, int role, @RequestParam(required = false) Integer communityId, @RequestParam(required = false) String country, @RequestParam(required = false) String organization){
        log.info("/inviteCode: {}, {}", email, role);
        inviteCodeService.generateCode(email, role, communityId, country, organization);
        return Result.success();
    }
}
