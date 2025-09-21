package org.sds.elevateconnect.controller;

import jakarta.annotation.Nullable;
import lombok.extern.slf4j.Slf4j;
import org.sds.elevateconnect.model.Result;
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

    @GetMapping("/manager/projects")
    public Result getProjectList(){
        return Result.success(projectService.getAllProjects());
    }

    @GetMapping("/manager/users")
    public Result getAllUsers() {
        return Result.success(userService.getAllUsers());
    }

    @DeleteMapping("/manager/users/{id}")
    public Result deleteUser(@PathVariable Integer id) {
        userService.deleteUser(id);
        return Result.success();
    }

    @PostMapping("/manager/sendInvitationCode")
    public Result sendInviteCode(String email, int role, @RequestParam(required = false) String country){
        log.info("/inviteCode: {}, {}", email, role);
        inviteCodeService.generateCode(email, role, country);
        return Result.success();
    }
}
