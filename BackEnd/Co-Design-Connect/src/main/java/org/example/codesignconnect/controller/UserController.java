package org.example.codesignconnect.controller;

import lombok.extern.slf4j.Slf4j;
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
    public Result login(String loginInfo, String password){
        Boolean success = userService.comparePassword(loginInfo, password);
        if(success) return Result.success();
        else return Result.error("Invalid username, email, or password");
    }

    @PostMapping("/signup")
    public Result signup(){
        return null;
    }
}
