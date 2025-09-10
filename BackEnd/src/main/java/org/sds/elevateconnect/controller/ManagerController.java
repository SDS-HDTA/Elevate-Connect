package org.sds.elevateconnect.controller;

import lombok.extern.slf4j.Slf4j;
import org.sds.elevateconnect.dto.ProjectDetail;
import org.sds.elevateconnect.model.Result;
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
    private ProjectService projectService;
    @Autowired
    private UserService userService;
    @Autowired
    private InviteCodeService inviteCodeService;

    @GetMapping("/manager/projects")
    public Result getProjectList(){
        List<ProjectDetail> projectList = projectService.getAllProjects();
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
    public Result sendInviteCode(String email, int type){
        log.info("/inviteCode: {}, {}", email, type);
        return inviteCodeService.generateCode(email, type);
    }
}
