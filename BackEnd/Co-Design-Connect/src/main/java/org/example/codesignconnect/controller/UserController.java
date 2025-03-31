package org.example.codesignconnect.controller;

import lombok.extern.slf4j.Slf4j;
import org.example.codesignconnect.dto.SignupRequest;
import org.example.codesignconnect.service.EmailService;
import org.example.codesignconnect.service.UserService;
import org.example.codesignconnect.model.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class UserController {
    @Autowired
    private UserService userService;

    @Autowired
    private EmailService emailService;

    @PostMapping("/login")
    public Result login(String email, String password){
        log.info("/login: {}, {}", email, password);
        return userService.login(email, password);
    }

    @PostMapping("/register")
    public Result signup(@RequestBody SignupRequest request){
        log.info("/register: {}", request);
        return userService.signup(request);
    }

    @PostMapping("/inviteCode")
    public Result generateCode(String email, Short type){
        log.info("/inviteCode: {}, {}", email, type);
        return userService.generateCode(email, type);
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
}
