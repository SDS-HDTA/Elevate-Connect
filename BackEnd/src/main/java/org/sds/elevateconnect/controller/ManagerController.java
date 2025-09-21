package org.sds.elevateconnect.controller;

import lombok.extern.slf4j.Slf4j;
import org.sds.elevateconnect.model.Result;
import org.sds.elevateconnect.model.project.Project;
import org.sds.elevateconnect.service.InviteCodeService;
import org.sds.elevateconnect.service.ProjectService;
import org.sds.elevateconnect.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
public class ManagerController {
    @Autowired
    private ProjectService ProjectService;
    @Autowired
    private UserService userService;
    @Autowired
    private InviteCodeService inviteCodeService;

    @GetMapping("/manager/projects")
    public Result getProjectList(){
        List<Project> projectList = ProjectService.getAllProjects();
        return Result.success(projectList);
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
    public Result sendInviteCode(String email, int role){
        log.info("/inviteCode: {}, {}", email, role);
        return inviteCodeService.generateCode(email, role);
    }
}
