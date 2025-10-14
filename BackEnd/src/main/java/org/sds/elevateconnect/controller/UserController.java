package org.sds.elevateconnect.controller;

import lombok.extern.slf4j.Slf4j;
import org.sds.elevateconnect.config.security.RequirePermission;
import org.sds.elevateconnect.dto.auth.AuthenticationResponse;
import org.sds.elevateconnect.dto.auth.CheckCodeRequest;
import org.sds.elevateconnect.dto.auth.ConfirmPasswordCodeRequest;
import org.sds.elevateconnect.dto.auth.LoginRequest;
import org.sds.elevateconnect.dto.auth.SignupRequest;
import org.sds.elevateconnect.dto.UserUpdateRequest;
import org.sds.elevateconnect.model.auth.Permission;
import org.sds.elevateconnect.model.auth.User;
import org.sds.elevateconnect.service.EmailService;
import org.sds.elevateconnect.model.Result;
import org.sds.elevateconnect.service.interfaces.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
public class UserController {
    @Autowired
    private IUserService userService;
    @Autowired
    private EmailService emailService;

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> login(LoginRequest request){
        return ResponseEntity.ok(userService.login(request));
    }

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> signup(@RequestBody SignupRequest request){
        log.info("/register: {}", request);

        return ResponseEntity.ok(userService.signup(request));
    }

    @PostMapping("/checkCode")
    public Result checkCode(@RequestBody CheckCodeRequest request){
        log.info("/checkCode: {}", request);

        return userService.checkCode(request);
    }

    @PostMapping("/password/resetCode")
    public Result sendVerificationCode(String email){
        log.info("/password/resetCode: {}", email);
        return emailService.sendVerificationCode(email);
    }

    @PostMapping("/password/update")
    public Result resetPassword(Integer userId, String newPassword){
        log.info("/password/update: {}, {}", userId, newPassword);
        return userService.resetPassword(userId, newPassword);
    }

     @PostMapping("/password/confirmCode")
    public Result confirmPasswordCode(@RequestBody ConfirmPasswordCodeRequest request){
        log.info("/password/confirmCode: {}", request);
        return userService.confirmPasswordCode(request);
    }

    @GetMapping("/user/info")
    public Result getUserInfo(@AuthenticationPrincipal User user){
        log.info("/user/info: {}", user.getId());
        return userService.getUserInfo(user.getId());
    }

    @GetMapping("/user/role")
    public Result getUserRole(@AuthenticationPrincipal User user) {
        log.info("/user/role: {}", user.getId());
        return Result.success(userService.getUserRoleById(user.getId()));
    }

    @RequirePermission(Permission.EDIT_USER)
    @PutMapping("/user")
    public void updateUser(@RequestBody UserUpdateRequest request)
     {
        log.info("/user: {}", request.getId());
        userService.updateUserById(request.getId(), request.getEmail(), request.getFirstName(), request.getLastName(), request.getPhone());
    }
}