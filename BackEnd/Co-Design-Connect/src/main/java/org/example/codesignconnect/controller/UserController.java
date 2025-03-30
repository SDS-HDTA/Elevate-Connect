package org.example.codesignconnect.controller;

import lombok.extern.slf4j.Slf4j;
import org.example.codesignconnect.dto.SignupRequest;
import org.example.codesignconnect.model.User;
import org.example.codesignconnect.service.UserService;
import org.example.codesignconnect.model.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public Result login(String email, String password){
        Boolean success = userService.comparePassword(email, password);
        if(success) return Result.success();
        else return Result.error("Invalid email or password");
    }

    @PostMapping("/signup")
    public Result signup(SignupRequest request){
        Boolean success = userService.signup(request);
        if(success) return Result.success();
        else return Result.error("Invalid Invite Code");
    }
}
