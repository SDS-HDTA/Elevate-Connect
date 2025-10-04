package org.sds.elevateconnect.controller;

import lombok.extern.slf4j.Slf4j;
import org.sds.elevateconnect.dto.auth.AuthenticationResponse;
import org.sds.elevateconnect.dto.auth.LoginRequest;
import org.sds.elevateconnect.dto.auth.SignupRequest;
import org.sds.elevateconnect.dto.UserUpdateRequest;
import org.sds.elevateconnect.service.EmailService;
import org.sds.elevateconnect.model.Result;
import org.sds.elevateconnect.service.interfaces.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<AuthenticationResponse> signup(SignupRequest request){
        log.info("/register: {}", request);

        return ResponseEntity.ok(userService.signup(request));
    }

    @PostMapping("/password/resetCode")
    public Result sendVerificationCode(String email){
        log.info("/password/resetCode: {}", email);
        return emailService.sendVerificationCode(email);
    }

    @PostMapping("/password/update")
    public Result resetPassword(String email, String verificationCode, String newPassword){
        log.info("/password/update: {}, {}, {}", email, verificationCode, newPassword);
        return userService.resetPassword(email, verificationCode, newPassword);
    }

    @GetMapping("/user/info")
    public Result getUserInfo(Integer userId){
        log.info("/user/info: {}", userId);
        return userService.getUserInfo(userId);
    }

    @GetMapping("/user/role")
    public Result getUserRole(Integer userId) {
        log.info("/user/role: {}", userId);
        return Result.success(userService.getUserRoleById(userId));
    }

    @PutMapping("/user")
    public void updateUser(@RequestBody UserUpdateRequest request)
     {
        log.info("/user: {}", request.getId());
        userService.updateUserById(request.getId(), request.getEmail(), request.getFirstName(), request.getLastName());
    }
}